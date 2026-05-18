# 005 Resignation Survival

Status: Draft

Depends on:
- `001-project-setup.md`
- `003-backend-simulation-api.md`

Owner:
- Planner

Reviewer:
- QA Reviewer

Write Scope:
- `docs/plans/005-resignation-survival.md`

Blocked By:
- MVP 서울 자취 가능성 API와 UI 흐름 안정화

Handoff Notes:
- 후속 도메인 Plan이며 구현 전 Ready 구체화가 필요하다.

## 작업 목표
사용자가 퇴사했을 때 현재 자산과 소비 습관 기준으로 몇 개월 동안 버틸 수 있는지 시뮬레이션한다.

## 구현 범위
- 퇴사 후 생존 가능 기간 계산 API
- 현재 현금성 자산, 월 고정비, 월 변동비, 예상 추가 수입 입력
- 생존 가능 개월 수와 위험도 산출
- 퇴사 전 필요한 비상금 해석 메시지 제공

## 제외 범위
- 사용자 자산 데이터 저장
- 실업급여 상세 계산
- 투자 자산 변동성 계산
- 세부 직군별 재취업 기간 예측

## 성공 조건
- 사용자는 현재 소비 습관 기준 생존 가능 기간을 확인할 수 있다.
- 응답은 숫자 결과와 해석 메시지를 함께 제공한다.
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
- 실업급여, 재취업 기간, 퇴사 후 보험료 변화를 별도 Plan으로 확장한다.
