# 008 Subscription Reality

Status: Draft

Depends on:
- `001-project-setup.md`
- `003-backend-simulation-api.md`

Owner:
- Planner

Reviewer:
- QA Reviewer

Write Scope:
- `docs/plans/008-subscription-reality.md`

Blocked By:
- MVP 서울 자취 가능성 API와 UI 흐름 안정화

Handoff Notes:
- 후속 도메인 Plan이며 구현 전 Ready 구체화가 필요하다.

## 작업 목표
사용자의 소득, 자산, 무주택 여부, 지역 조건을 기준으로 청약이 현실적으로 의미 있는 선택인지 분석한다.

## 구현 범위
- 청약 현실성 분석 API
- 소득, 자산, 무주택 기간, 부양가족 수, 희망 지역 입력
- 청약 준비 필요성, 자금 부족 여부, 우선순위 해석
- 결과 리포트 제공

## 제외 범위
- 실제 청약 신청
- 청약 당첨 확률 보장
- 모든 특별공급 유형 상세 판정
- 법률 또는 세무 자문

## 성공 조건
- 사용자는 현재 조건에서 청약 준비가 의미 있는지 확인할 수 있다.
- 응답은 조건별 부족 요소와 해석 메시지를 포함한다.
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
- 청약 공고 데이터, 특별공급 조건, 지역별 분양가 데이터를 별도 Plan으로 확장한다.
