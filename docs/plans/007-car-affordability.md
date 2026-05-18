# 007 Car Affordability

Status: Draft

Depends on:
- `001-project-setup.md`
- `003-backend-simulation-api.md`

Owner:
- Planner

Reviewer:
- QA Reviewer

Write Scope:
- `docs/plans/007-car-affordability.md`

Blocked By:
- MVP 서울 자취 가능성 API와 UI 흐름 안정화

Handoff Notes:
- 후속 도메인 Plan이며 구현 전 Ready 구체화가 필요하다.

## 작업 목표
사용자의 소득, 생활비, 차량 비용 기준으로 자차 유지가 가능한지 시뮬레이션한다.

## 구현 범위
- 자차 유지 가능성 계산 API
- 차량 할부금, 보험료, 유류비, 주차비, 정비비 입력
- 월 순소득 대비 차량 유지비 비중 계산
- 유지 가능성, 저축 영향, 위험도 해석 메시지 제공

## 제외 범위
- 차량 구매 추천
- 중고차 시세 조회
- 보험료 실시간 견적
- 세부 차종별 감가상각 계산

## 성공 조건
- 사용자는 자차 유지비가 월 현금흐름에 미치는 영향을 확인할 수 있다.
- 응답은 비용 비중과 해석 메시지를 함께 제공한다.
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
- 차종별 유지비 프리셋과 공공/외부 데이터 기반 평균 비용을 별도 Plan으로 확장한다.
