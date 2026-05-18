# 003 Backend Simulation API

Status: Draft

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
- JDK 17 이상 로컬 환경 구성

Handoff Notes:
- `002-frontend-simulation-form.md`의 입력 스키마와 Mock 결과 타입을 참고해 API 계약을 확정한다.

## 작업 목표
서울 자취 가능성 시뮬레이션을 수행하는 백엔드 API를 구현한다.

프론트엔드 Mock UI에서 검증한 입력 흐름과 결과 리포트 구조를 기준으로 실제 API 계약과 계산 로직을 확정한다.

## 구현 범위
- 시뮬레이션 요청 DTO 정의
- Bean Validation 기반 입력 검증
- 시뮬레이션 응답 DTO 정의
- Controller, Service, Calculator 분리
- MVP용 계산 및 해석 로직 구현
- 프론트엔드에서 사용할 API 계약 제공

## 제외 범위
- 사용자 연봉/자산 데이터 저장
- 로그인 또는 세션 기반 기능
- 실제 공공데이터 자동 수집
- 프론트엔드 화면 설계
- Mock UI 자체 구현
- 퇴사, 월세 vs 전세, 자차 유지, 청약, 지원금 추천 시뮬레이션

## API 계약 초안
Endpoint:
- `POST /api/simulations/seoul-living`

Request:
- `annualSalary`
- `monthlyRent`
- `deposit`
- `district`
- `housingType`
- `monthlyLivingCost`
- `monthlySavingGoal`

Response:
- `available`
- `monthlyNetIncome`
- `housingCostRatio`
- `expectedMonthlySavings`
- `riskLevel`
- `messages`

API 계약은 `002-frontend-simulation-form.md`의 Mock 입력 흐름과 결과 리포트 타입을 참고해 확정한다.

## 성공 조건
- Controller는 요청/응답만 담당한다.
- 계산 로직은 Service 또는 Calculator에 분리되어 있다.
- DTO와 Entity가 분리되어 있다.
- 잘못된 입력은 검증 에러로 응답한다.
- 응답에는 단순 숫자뿐 아니라 해석 메시지가 포함된다.

## 테스트 조건
- `./gradlew test`
- `./gradlew build`

테스트에는 최소한 DTO 검증, 계산 로직, API 응답 흐름을 포함한다.

## 변경 파일 목록
예상 변경 파일:
- `backend/build.gradle`
- `backend/src/main/java`
- `backend/src/test/java`

실제 변경 파일:
- 구현 후 실제 변경 파일 목록으로 갱신한다.

## 검증 결과
Status:
- [ ] Not Run
- [ ] Passed
- [ ] Failed
- [ ] Skipped

실행 명령:
- 

결과:
- 

생략 사유:
- 

## 구현 요약
구현 완료 후 작성한다.

## 후속 작업
- `005-resignation-survival.md`에서 퇴사 후 생존 가능 기간 API를 구현한다.
- `006-rent-vs-jeonse.md`에서 월세 vs 전세 비교 API를 구현한다.
- `007-car-affordability.md`에서 자차 유지 가능성 API를 구현한다.
- `008-subscription-reality.md`에서 청약 현실성 API를 구현한다.
- `009-government-support.md`에서 정부지원금 또는 보조금 추천 API를 구현한다.
