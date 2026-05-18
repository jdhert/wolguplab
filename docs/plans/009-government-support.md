# 009 Government Support

Status: Draft

Depends on:
- `001-project-setup.md`
- `003-backend-simulation-api.md`

Owner:
- Planner

Reviewer:
- QA Reviewer

Write Scope:
- `docs/plans/009-government-support.md`

Blocked By:
- MVP 서울 자취 가능성 API와 UI 흐름 안정화

Handoff Notes:
- 후속 도메인 Plan이며 구현 전 Ready 구체화가 필요하다.

## 작업 목표
사용자의 소득, 가구 조건, 주거 상태를 기준으로 확인해볼 만한 정부지원금 또는 보조금 후보를 추천한다.

## 구현 범위
- 지원금 추천 API
- 소득, 가구원 수, 주거 형태, 지역, 나이대 입력
- 조건 기반 지원금 후보 목록 산출
- 신청 전 확인해야 할 해석 메시지 제공

## 제외 범위
- 실제 지원금 신청
- 지원금 수급 보장
- 모든 지자체 정책 실시간 반영
- 법률 또는 행정 자문

## 성공 조건
- 사용자는 자신의 조건에서 확인할 만한 지원금 후보를 볼 수 있다.
- 응답은 추천 후보와 확인 필요 조건을 함께 제공한다.
- 입력값은 백엔드에서 검증한다.
- 프론트엔드는 계산하지 않고 백엔드 응답을 표시한다.

## 테스트 조건
Frontend:
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`

Backend:
- `./gradlew test`
- `./gradlew build`

## 변경 파일 목록
예상 변경 파일:
- `frontend/app`
- `frontend/features`
- `frontend/schemas`
- `frontend/types`
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
- 공공데이터 기반 정책 목록 동기화와 지역별 정책 갱신을 별도 Plan으로 확장한다.
