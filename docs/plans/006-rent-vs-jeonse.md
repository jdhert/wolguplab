# 006 Rent vs Jeonse

Status: Draft

Depends on:
- `001-project-setup.md`
- `003-backend-simulation-api.md`

Owner:
- Planner

Reviewer:
- QA Reviewer

Write Scope:
- `docs/plans/006-rent-vs-jeonse.md`
- future frontend/backend scope

Blocked By:
- MVP 서울 자취 가능성 API와 UI 흐름 안정화

Handoff Notes:
- Kakao Maps JavaScript SDK를 MVP 기본 지도 API로 사용한다.
- 구현 전 지도 표시용 데이터 계약과 환경 변수 기준을 구체화해야 한다.

## 작업 목표
사용자의 지역, 보증금, 대출 한도, 월 현금흐름 기준으로 월세와 전세 중 어떤 선택이 유리한지 비교한다.

사용자는 지도에서 서울 구 또는 동 단위 지역을 시각적으로 선택하고, 선택한 지역의 월세/전세 부담 차이를 확인할 수 있어야 한다.

## 구현 범위
- 월세 vs 전세 비교 API
- 지역, 보증금, 월세, 전세금, 대출 한도, 금리 입력
- 월 현금흐름과 총 주거비 비교
- 사용자 조건에 맞는 해석 메시지 제공
- Kakao Maps JavaScript SDK 기반 지도 화면
- 서울 구 또는 동 단위 지역 선택 UI
- 지역별 월세/전세 유불리 결과를 지도 색상, 마커, 툴팁 또는 결과 패널로 표시

## 지도 API 선택
MVP 기본 지도 API는 Kakao Maps JavaScript SDK를 사용한다.

선택 이유:
- 한국 주소와 행정구역 기반 사용자 경험에 적합하다.
- 서울 구/동 단위 지역 선택과 시각화에 충분하다.
- 프론트엔드에서 지도 렌더링, 마커, 오버레이 구성이 가능하다.
- 주소 좌표 변환, 좌표 행정구역 변환 등 로컬 API 확장 여지가 있다.

대안:
- Naver Maps는 한국 지도 품질이 좋아 대안으로 유지한다.
- Google Maps는 한국 부동산/생활권 MVP에서는 우선순위를 낮게 둔다.

## 책임 분리
Backend:
- 지역별 월세/전세 비용 비교 계산
- 사용자 보증금, 대출 한도, 금리, 월 현금흐름 기준 유불리 판단
- `riskLevel`, `affordabilityScore`, 해석 메시지 생성
- 지도 표시용 지역별 요약 데이터 제공

Frontend:
- Kakao Maps 렌더링
- 지역 선택, 지도 색상, 마커, 툴팁, 결과 패널 표시
- API 응답 타입 정의와 화면 표시
- 계산 로직 구현 금지

## 제외 범위
- 실제 대출 심사
- 실시간 금리 조회
- 계약 리스크 법률 판단
- 개별 매물 추천
- 지도 API를 이용한 비용 계산
- 사용자 위치 추적 기반 추천

## 성공 조건
- 사용자는 자신의 한도 기준으로 월세와 전세 부담을 비교할 수 있다.
- 사용자는 지도에서 지역을 선택하고 해당 지역의 비교 결과를 확인할 수 있다.
- 응답은 월 비용, 총 비용, 리스크 해석을 포함한다.
- 지도는 백엔드가 계산한 지역별 결과를 시각화만 한다.
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
- `frontend/components`
- `frontend/lib`
- `frontend/schemas`
- `frontend/types`
- `backend/src/main/java`
- `backend/src/test/java`
- `.env.example`

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
- 국토교통부 전월세 실거래가 기반 지역 평균 비교를 별도 Plan으로 확장한다.
