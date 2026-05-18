# 003 Backend Simulation API

Status: Ready

Depends on:
- `001-project-setup.md`
- `002-frontend-simulation-form.md`

Owner:
- Backend Worker

Reviewer:
- QA Reviewer

Write Scope:
- `backend/src/main/java`
- `backend/src/test/java`
- `backend/build.gradle`

Blocked By:
- JDK 17 이상 로컬 실행 환경 확인

Handoff Notes:
- 이 Plan은 API 계약, 검증 규칙, 계산 규칙, 패키지 구조, 테스트 조건이 정리되어 `Ready` 상태다.
- 구현은 JDK 17 이상이 준비되고 backend Gradle 명령이 실행 가능한 상태에서 시작한다.
- `frontend/features/seoul-living/schema.ts`와 `frontend/features/seoul-living/types.ts`를 API 계약의 기준으로 삼는다.
- 이전 초안의 `housingType`, `available`, `messages` 필드는 MVP 계약에서 제외한다.
- 사용자 연봉, 자산, 월세 입력값은 MVP에서 저장하지 않는다.

## 작업 목표
서울 자취 가능성 시뮬레이션을 수행하는 백엔드 API를 구현한다.

프론트엔드 Mock UI에서 검증한 입력 흐름과 결과 리포트 구조를 기준으로 실제 API 계약, 입력 검증, 계산 로직, 해석 메시지를 제공한다.

## 구현 범위
- `POST /api/simulations/seoul-living` API 구현
- 요청 DTO 정의
- 응답 DTO 정의
- Bean Validation 기반 입력 검증
- 공통 validation error response 정의
- Controller, Service, Calculator 책임 분리
- 서울 자치구와 생활 스타일 도메인 enum 정의
- MVP용 실수령 추정, 월세 비중, 예상 저축액, 위험도 계산
- 계산, 서비스, 컨트롤러 테스트 작성

## 제외 범위
- 프론트엔드 UI 변경
- 프론트엔드 API 연동
- Docker 또는 Docker Compose 구현
- Kakao Maps 실제 연동
- 외부 공공데이터 API 호출
- 구별 평균 월세 자동 조회
- 로그인, 세션, 사용자 계정
- 사용자 금융 입력값 저장
- Entity, Repository, migration 파일
- 실제 급여세금 계산 엔진
- 실제 API key, DB password, secret 작성
- 퇴사, 월세 vs 전세, 자차 유지, 청약, 지원금 추천 시뮬레이션

## API Contract
Endpoint:

```http
POST /api/simulations/seoul-living
Content-Type: application/json
```

### Request DTO
Class name:
- `SeoulLivingSimulationRequest`

Rules:
- 모든 금액 필드는 KRW 원 단위 정수다.
- 금액 필드는 Java `Long`을 사용한다.
- `district`와 `lifestyle`은 프론트엔드의 한글 label을 그대로 받는다.
- `district`는 서비스 또는 도메인 계층에서 `SeoulDistrict`로 검증한다.
- `lifestyle`은 서비스 또는 도메인 계층에서 `LifestyleType`으로 검증한다.
- 잘못된 `district` 또는 `lifestyle` 값은 validation error response로 응답한다.
- `housingType`은 MVP 요청 필드에 포함하지 않는다.

| JSON field | Java type | Required | Validation |
|---|---:|---:|---|
| `annualSalary` | `Long` | yes | `1 <= value <= 500_000_000` |
| `district` | `String` | yes | 서울 25개 자치구 label 중 하나 |
| `deposit` | `Long` | yes | `0 <= value <= 2_000_000_000` |
| `monthlyRent` | `Long` | yes | `0 <= value <= 20_000_000` |
| `monthlyLivingCost` | `Long` | yes | `0 <= value <= 20_000_000` |
| `monthlySavingGoal` | `Long` | yes | `0 <= value <= 20_000_000` |
| `lifestyle` | `String` | yes | `절약형`, `균형형`, `여유형` 중 하나 |

Supported districts:
- `강남구`, `강동구`, `강북구`, `강서구`, `관악구`
- `광진구`, `구로구`, `금천구`, `노원구`, `도봉구`
- `동대문구`, `동작구`, `마포구`, `서대문구`, `서초구`
- `성동구`, `성북구`, `송파구`, `양천구`, `영등포구`
- `용산구`, `은평구`, `종로구`, `중구`, `중랑구`

Example request:

```json
{
  "annualSalary": 60000000,
  "district": "마포구",
  "deposit": 10000000,
  "monthlyRent": 900000,
  "monthlyLivingCost": 1500000,
  "monthlySavingGoal": 700000,
  "lifestyle": "균형형"
}
```

### Response DTO
Class name:
- `SeoulLivingSimulationResponse`

Response shape must mirror `SeoulLivingReport`.

| JSON field | Java type | Rule |
|---|---:|---|
| `grade` | `String` | `가능`, `주의 필요`, `위험` 중 하나 |
| `headline` | `String` | 위험도별 해석 문구 |
| `districtLabel` | `String` | 요청 `district` label echo |
| `riskLevel` | `SeoulLivingRiskLevel` | `LOW`, `MEDIUM`, `HIGH` |
| `monthlyNetIncome` | `Long` | 추정 월 실수령액 |
| `housingCostRatio` | `Integer` | 월세 비중, 정수 percent |
| `expectedMonthlySavings` | `Long` | `monthlyNetIncome - monthlyRent - monthlyLivingCost` |
| `warnings` | `List<String>` | 최소 1개 이상 |
| `recommendations` | `List<String>` | 위험도 추천 1개 + 생활 스타일 추천 1개 |

Example response:

```json
{
  "grade": "가능",
  "headline": "현재 조건에서는 서울 자취가 비교적 안정적입니다.",
  "districtLabel": "마포구",
  "riskLevel": "LOW",
  "monthlyNetIncome": 4100000,
  "housingCostRatio": 22,
  "expectedMonthlySavings": 1700000,
  "warnings": ["현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다."],
  "recommendations": [
    "현재 수준을 유지하되 비상금과 이사 초기 비용을 별도로 준비하세요.",
    "균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요."
  ]
}
```

### Validation Error Response
Validation 실패, 지원하지 않는 자치구, 지원하지 않는 생활 스타일, 정수 형식 오류, field mapping 가능한 malformed body는 HTTP 400으로 응답한다.

```json
{
  "code": "VALIDATION_ERROR",
  "message": "입력값을 확인해주세요.",
  "fieldErrors": [
    {
      "field": "annualSalary",
      "message": "연봉을 입력해주세요."
    }
  ]
}
```

Rules:
- `fieldErrors[].field`는 request JSON field 이름을 사용한다.
- `fieldErrors[].message`는 사용자에게 보여줄 수 있는 한국어 문구를 사용한다.
- 여러 field error를 한 번에 반환할 수 있다.
- stack trace, Java exception class name은 응답에 노출하지 않는다.

## Calculation Rules
MVP 계산은 결정적이고 단순한 추정 로직이다. 실제 급여세금 계산 엔진이 아니다.

### Monthly Net Income
연봉 구간별 multiplier:

| Gross annual salary range | Multiplier |
|---:|---:|
| `1 <= annualSalary <= 30_000_000` | `0.88` |
| `30_000_001 <= annualSalary <= 50_000_000` | `0.85` |
| `50_000_001 <= annualSalary <= 70_000_000` | `0.82` |
| `70_000_001 <= annualSalary <= 500_000_000` | `0.78` |

Formula:

```text
monthlyNetIncome = max(1, round(annualSalary * multiplier / 12))
```

### Housing Cost Ratio
Formula:

```text
if monthlyRent == 0:
  housingCostRatio = 0
else:
  housingCostRatio = round(monthlyRent / monthlyNetIncome * 100)
```

### Expected Monthly Savings
Formula:

```text
expectedMonthlySavings = monthlyNetIncome - monthlyRent - monthlyLivingCost
```

Rules:
- `monthlySavingGoal`은 `expectedMonthlySavings`에서 차감하지 않는다.
- `monthlySavingGoal`은 목표 저축액 달성 가능 여부를 판단할 때 별도로 비교한다.
- `expectedMonthlySavings`는 음수가 될 수 있다.

### Risk Level
아래 순서대로 판단한다.

1. `LOW` / `가능`
   - `housingCostRatio <= 25`
   - `expectedMonthlySavings >= monthlySavingGoal`
   - `expectedMonthlySavings >= round(monthlyNetIncome * 0.15)`
2. `MEDIUM` / `주의 필요`
   - `LOW`가 아니고
   - `housingCostRatio <= 35`
   - `expectedMonthlySavings >= 0`
3. `HIGH` / `위험`
   - 그 외 모든 경우

Headline:
- `LOW`: `현재 조건에서는 서울 자취가 비교적 안정적입니다.`
- `MEDIUM`: `서울 자취는 가능하지만 월세와 소비 조정이 필요합니다.`
- `HIGH`: `현재 조건에서는 서울 자취 부담이 커서 조건 조정이 필요합니다.`

### Warning Rules
아래 순서대로 warning을 추가한다.

1. `housingCostRatio > 30`: `월세 비중이 실수령 대비 높습니다.`
2. `expectedMonthlySavings < 0`: `월세와 생활비가 월 실수령을 초과합니다.`
3. `expectedMonthlySavings >= 0` and `expectedMonthlySavings < monthlySavingGoal`: `현재 소비 구조에서는 목표 저축액 달성이 어렵습니다.`
4. `deposit < 5_000_000`: `초기 보증금 여유가 낮아 선택 가능한 매물이 제한될 수 있습니다.`
5. 위 조건이 하나도 없으면: `현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다.`

### Recommendation Rules
항상 위험도 기반 추천 1개를 먼저 추가한다.

- `LOW`: `현재 수준을 유지하되 비상금과 이사 초기 비용을 별도로 준비하세요.`
- `MEDIUM`: `월세와 생활비를 함께 조정해 목표 저축액을 먼저 확보해보세요.`
- `HIGH`: `월세 상한을 낮추거나 희망 지역을 조정한 뒤 다시 계산해보세요.`

항상 생활 스타일 기반 추천 1개를 두 번째로 추가한다.

- `절약형`: `절약형 생활을 유지하되 고정비가 늘어나는 항목을 먼저 점검하세요.`
- `균형형`: `균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요.`
- `여유형`: `여유형 생활을 원한다면 월세 상한을 더 보수적으로 잡는 것이 안전합니다.`

MVP에서 `lifestyle`은 추천 문구에만 영향을 주며 수치 기준은 바꾸지 않는다.

## Backend Package and File Structure
Package root:
- `com.wolguplab.backend`

Expected implementation files:
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/controller/SeoulLivingSimulationController.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/dto/SeoulLivingSimulationRequest.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/dto/SeoulLivingSimulationResponse.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/domain/SeoulDistrict.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/domain/LifestyleType.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/domain/SeoulLivingRiskLevel.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/service/SeoulLivingSimulationService.java`
- `backend/src/main/java/com/wolguplab/backend/simulation/seoulliving/calculator/SeoulLivingCalculator.java`
- `backend/src/main/java/com/wolguplab/backend/common/error/ApiErrorResponse.java`
- `backend/src/main/java/com/wolguplab/backend/common/error/FieldErrorResponse.java`
- `backend/src/main/java/com/wolguplab/backend/common/error/GlobalExceptionHandler.java`
- `backend/src/main/java/com/wolguplab/backend/common/error/InvalidSimulationInputException.java`

Expected test files:
- `backend/src/test/java/com/wolguplab/backend/simulation/seoulliving/controller/SeoulLivingSimulationControllerTest.java`
- `backend/src/test/java/com/wolguplab/backend/simulation/seoulliving/service/SeoulLivingSimulationServiceTest.java`
- `backend/src/test/java/com/wolguplab/backend/simulation/seoulliving/calculator/SeoulLivingCalculatorTest.java`

Forbidden in Plan 003:
- `backend/src/main/java/**/entity/**`
- `backend/src/main/java/**/repository/**`
- migration files
- Dockerfiles
- `docker-compose.yml`
- frontend UI files

## Test Cases
Calculator tests:
- Salary tier boundaries: `30_000_000`, `30_000_001`, `50_000_000`, `50_000_001`, `70_000_000`, `70_000_001`.
- `annualSalary = 1` returns `monthlyNetIncome = 1`.
- `monthlyRent = 0` returns `housingCostRatio = 0`.
- Example request returns `monthlyNetIncome = 4_100_000`, `housingCostRatio = 22`, `expectedMonthlySavings = 1_700_000`, `riskLevel = LOW`, `grade = 가능`.
- Deterministic `MEDIUM` case.
- Deterministic `HIGH` case.
- Warning order and fallback warning.
- Recommendation order: risk-level recommendation first, lifestyle recommendation second.

Service/domain tests:
- All 25 Seoul district labels are accepted.
- Unknown district, e.g. `부산진구`, is rejected with field `district`.
- `절약형`, `균형형`, `여유형` are accepted.
- Unknown lifestyle, e.g. `극단형`, is rejected with field `lifestyle`.
- `districtLabel` equals request `district`.
- `lifestyle` changes recommendation text only.
- No persistence is used.

Controller/API tests:
- Valid request returns HTTP 200 and all `SeoulLivingReport` fields.
- Valid example request returns exact example response values.
- Missing `annualSalary` returns HTTP 400 with `code = VALIDATION_ERROR`.
- `annualSalary <= 0` returns HTTP 400.
- `annualSalary > 500_000_000` returns HTTP 400.
- Negative `deposit`, `monthlyRent`, `monthlyLivingCost`, or `monthlySavingGoal` returns HTTP 400.
- Decimal money value returns HTTP 400.
- Invalid `district` returns HTTP 400.
- Invalid `lifestyle` returns HTTP 400.
- Malformed JSON returns HTTP 400 without stack traces.
- Tests must not require PostgreSQL.

## 성공 조건
- Controller는 요청/응답 조립만 담당한다.
- 계산 로직은 Calculator에 분리한다.
- Service는 request validation, domain 변환, calculator 호출, response 조립을 담당한다.
- DTO와 domain enum 책임을 분리한다.
- 잘못된 입력은 안정적인 HTTP 400 validation error response로 응답한다.
- 응답은 단순 숫자뿐 아니라 생활 해석 리포트 형태의 문구를 포함한다.
- 프론트엔드 Mock response type과 호환된다.

## 테스트 조건
- `./gradlew test`
- `./gradlew build`

JDK 17 이상이 준비되지 않았으면:
- 구현을 시작하지 않는다.
- 이미 구현 중인 상태에서 발견되면 검증 결과를 `Skipped` 또는 `Failed`로 기록한다.
- 정확한 실패 사유를 Plan에 남긴다.
- `Done`으로 올리지 않는다.

## 변경 파일 목록
예상 변경 파일:
- `backend/build.gradle`
- `backend/src/main/java`
- `backend/src/test/java`

실제 변경 파일:
- 구현 후 실제 변경 파일 목록으로 갱신한다.

## 검증 결과
Status:
- [x] Not Run
- [ ] Passed
- [ ] Failed
- [ ] Skipped

실행 명령:
- 

결과:
- 

생략 사유:
- 구현 전 Plan 정리 단계다.

## 구현 요약
아직 구현하지 않았다.

## 후속 작업
- JDK 17 이상 환경을 확인한 뒤 Backend Worker에게 이 Plan을 배정한다.
- `004-docker-compose.md`는 Plan 003 구현과 검증 이후 Ready 여부를 다시 판단한다.
- `005-resignation-survival.md`에서 퇴사 후 생존 가능 기간 API를 구현한다.
- `006-rent-vs-jeonse.md`에서 월세 vs 전세 비교 API를 구현한다.
- `007-car-affordability.md`에서 자차 유지 가능성 API를 구현한다.
- `008-subscription-reality.md`에서 청약 현실성 API를 구현한다.
- `009-government-support.md`에서 정부지원금 또는 보조금 추천 API를 구현한다.
