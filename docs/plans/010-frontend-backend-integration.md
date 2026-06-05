# 010 Frontend Backend Integration

Status: Ready

Depends on:
- `002-frontend-simulation-form.md`
- `003-backend-simulation-api.md`

Owner:
- Frontend Worker

Reviewer:
- QA Reviewer

Write Scope:
- `frontend/features/seoul-living`
- `frontend/lib`
- `frontend/app`
- `.env.example`
- `docs/plans/010-frontend-backend-integration.md`

Blocked By:
- 없음

Handoff Notes:
- `002-frontend-simulation-form.md`의 Mock UI와 `003-backend-simulation-api.md`의 실제 API를 연결한다.
- 프론트 입력 단위는 `만원`이고 backend API 계약은 KRW 원 단위 정수이므로 요청 직전에 금액 필드를 `* 10_000` 변환한다.
- 응답은 `SeoulLivingReport`와 호환되므로 기존 리포트 UI를 최대한 유지한다.
- 이 Plan은 API 연동 범위가 정리되어 `Ready` 상태다.

## 작업 목표
서울 자취 가능성 프론트엔드 Mock 결과 생성을 실제 backend API 호출로 교체한다.

사용자는 기존 단계형 입력 UX를 그대로 사용하고, 마지막 단계에서 `POST /api/simulations/seoul-living` backend API를 호출해 실제 계산 결과 리포트를 확인할 수 있어야 한다.

## 구현 범위
- 서울 자취 가능성 submit 흐름에서 `createMockSeoulLivingReport` 직접 호출 제거 또는 fallback-only 격리
- backend API 호출 함수 추가
- 프론트 입력값을 backend request DTO로 변환
- 금액 단위 변환: `만원` 입력값을 KRW 원 단위 정수로 변환
- backend response를 `SeoulLivingReport` 타입으로 표시
- loading 상태 추가
- API 실패 상태 추가
- 재시도 또는 다시 입력 동선 유지
- backend base URL 환경 변수 정책 정리
- 연동 테스트 추가 또는 기존 테스트 보강

## 제외 범위
- 백엔드 API 계약 변경
- 백엔드 계산 로직 변경
- Docker Compose 구현
- Kakao Maps 연동
- 공공데이터 조회
- 로그인, 결과 저장, 사용자 세션
- 다른 시뮬레이션 화면 연동
- 디자인 전면 개편

## API Contract
Backend endpoint:

```http
POST /api/simulations/seoul-living
Content-Type: application/json
```

Backend request fields:
- `annualSalary`
- `district`
- `deposit`
- `monthlyRent`
- `monthlyLivingCost`
- `monthlySavingGoal`
- `lifestyle`

Backend response fields must match `SeoulLivingReport`:
- `grade`
- `headline`
- `districtLabel`
- `riskLevel`
- `monthlyNetIncome`
- `housingCostRatio`
- `expectedMonthlySavings`
- `warnings`
- `recommendations`

## Unit Conversion
Current frontend form labels use `만원`.

Before API request, convert these fields to KRW integer values:
- `annualSalary * 10_000`
- `deposit * 10_000`
- `monthlyRent * 10_000`
- `monthlyLivingCost * 10_000`
- `monthlySavingGoal * 10_000`

Do not convert:
- `district`
- `lifestyle`

Backend response values are already KRW integer values and should be displayed through `formatKrw`.

## Environment Policy
Recommended frontend environment variable:

```dotenv
NEXT_PUBLIC_BACKEND_API_BASE_URL=
```

Rules:
- Local development can set `NEXT_PUBLIC_BACKEND_API_BASE_URL=http://localhost:8080`.
- Repository templates must keep the value blank or placeholder-only.
- Do not write real secrets.
- This value is browser-exposed, so it must not contain secret credentials.
- If the implementation chooses a Next.js proxy route instead, document the proxy route and keep backend secrets server-side only.

## UI/UX 흐름
- 사용자는 기존 `/simulations/seoul-living` 단계형 입력 흐름을 그대로 사용한다.
- 마지막 단계의 `리포트 보기` 클릭 시 backend API 호출을 시작한다.
- 요청 중에는 버튼 중복 클릭을 막고 loading 상태를 보여준다.
- 성공 시 기존 리포트 UI에 backend 응답을 표시한다.
- 실패 시 사용자가 이해할 수 있는 오류 상태를 보여주고, 다시 시도하거나 입력을 수정할 수 있게 한다.
- `다시 실험하기` 동선은 유지한다.
- 결과 화면의 `Mock Report` 문구는 실제 API 결과에 맞게 제거하거나 변경한다.

## Error Handling
API 실패 유형:
- network error
- backend HTTP 400 validation error
- backend HTTP 5xx error
- response shape mismatch

Required behavior:
- HTTP 400 validation error는 가능하면 사용자에게 입력 확인 메시지로 보여준다.
- HTTP 5xx와 network error는 서비스 오류 메시지로 보여준다.
- 오류 발생 시 기존 입력값은 유지한다.
- 실패 상태가 결과 리포트처럼 오해되지 않게 한다.

## 성공 조건
- 프론트엔드는 더 이상 기본 성공 흐름에서 Mock report를 사용하지 않는다.
- submit 시 backend API를 호출한다.
- request payload가 backend API 계약과 일치한다.
- 금액 필드는 KRW 원 단위 정수로 변환되어 전송된다.
- backend 응답은 기존 `SeoulLivingReport` 기반 UI에 표시된다.
- loading, success, error 상태가 모두 동작한다.
- 기존 단계형 UX와 반응형 레이아웃이 유지된다.
- 프론트엔드에 계산 로직을 추가하지 않는다.
- 실제 API key 또는 secret을 커밋하지 않는다.

## 테스트 조건
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`

테스트에는 최소한 다음을 포함한다.
- form values를 backend request payload로 변환하는 단위 테스트
- `만원` to KRW 변환 테스트
- API 성공 시 report 표시 테스트 또는 submit flow 테스트
- API 실패 시 error 상태 테스트

## 변경 파일 목록
예상 변경 파일:
- `frontend/features/seoul-living/seoul-living-simulator.tsx`
- `frontend/features/seoul-living/api.ts`
- `frontend/features/seoul-living/request.ts`
- `frontend/features/seoul-living/request.test.ts`
- `frontend/features/seoul-living/seoul-living-simulator.test.tsx`
- `frontend/features/seoul-living/mock-report.ts`
- `frontend/features/seoul-living/types.ts`
- `frontend/lib`
- `.env.example`
- `docs/plans/010-frontend-backend-integration.md`

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
- 구현 완료 후 `004-docker-compose.md`에서 frontend와 backend를 함께 실행하는 Compose 구성을 Ready로 승격할지 판단한다.
- API base URL과 local 개발 실행 방법을 README 또는 별도 문서에 정리할 수 있다.
