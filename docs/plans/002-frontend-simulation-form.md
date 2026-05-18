# 002 Frontend Simulation UX

Status: Done

Depends on:
- `001-project-setup.md`

Owner:
- Frontend Worker

Reviewer:
- QA Reviewer

Write Scope:
- `frontend/app`
- `frontend/components`
- `frontend/features`
- `frontend/lib`
- `frontend/schemas`
- `frontend/types`

Blocked By:
- 없음

Handoff Notes:
- Mock UI MVP는 완료되었다.
- Backend API는 이 Plan의 입력 스키마와 Mock 결과 타입을 참고해 계약을 확정한다.

## 작업 목표
월급실험실 MVP의 프론트엔드 UI/UX 흐름을 Mock 데이터 기반으로 먼저 완성한다.

메인 화면은 여러 시뮬레이션을 카드 형태로 보여주고, 사용자는 서울 자취 가능성 카드를 선택해 단계형 입력 UX와 결과 리포트 Mock UI를 확인할 수 있어야 한다.

## 구현 범위
- 메인 카드형 시뮬레이션 선택 화면
- 서울 자취 가능성 독립 페이지
- 단계형 또는 질문형 입력 UX
- React Hook Form + Zod 기반 입력 검증
- Mock 데이터 기반 결과 리포트 UI
- 반응형 레이아웃
- 기본 디자인 시스템 정리
- API 응답으로 교체 가능한 프론트 타입 정의

## 제외 범위
- 실제 백엔드 API 연동
- 실제 계산 로직 구현
- 실제 공공데이터 직접 조회
- 로그인 또는 사용자 정보 저장
- 결과 저장 기능
- 퇴사, 월세 vs 전세, 자차 유지, 청약, 지원금 추천 상세 화면

## UI/UX 방향
- 계산기 사이트처럼 보이는 긴 입력폼을 만들지 않는다.
- 엑셀, 관리자 페이지, ERP, 통계 대시보드, 공공기관 사이트 느낌을 피한다.
- 메인 페이지는 탭보다 카드형 시뮬레이션 선택 구조를 우선한다.
- 입력은 한 화면에 모두 노출하지 않고 단계형 또는 질문형으로 나눈다.
- 결과는 숫자 출력이 아니라 생활 분석 리포트처럼 보여준다.
- 쉬운 금융 UX, 리포트 UI, 생활 친화적인 감성을 지향한다.

## 라우트
- `/`: 카드형 시뮬레이션 선택 화면
- `/simulations/seoul-living`: 서울 자취 가능성 시뮬레이션

후속 라우트는 카드만 노출하고 상세 구현은 별도 Plan에서 진행한다.

- `/simulations/resignation`
- `/simulations/car`
- `/simulations/rent-vs-jeonse`
- `/simulations/subscription`
- `/simulations/government-support`

## 메인 카드 초안
- 서울 자취 가능성
- 퇴사 생존 분석
- 자차 유지 가능성
- 월세 vs 전세 비교
- 청약 현실성 분석
- 지원금 추천

MVP에서는 서울 자취 가능성 카드만 활성화하고, 나머지는 준비 중 상태로 표시할 수 있다.

## 서울 자취 입력 흐름 초안
1. 연봉은 얼마인가요?
2. 희망 지역은 어디인가요?
3. 현재 보유 보증금은 얼마인가요?
4. 예상 월세 또는 감당 가능한 월세는 얼마인가요?
5. 월 생활비는 어느 정도인가요?
6. 매달 목표 저축액이 있나요?
7. 생활 스타일은 어떤 편인가요?

입력 항목은 실제 백엔드 API 계약 확정 시 갱신한다.

## 결과 리포트 구성
- 가능성 등급
- 예상 월 실수령
- 예상 주거비 비중
- 예상 생활비
- 예상 저축 가능 금액
- 주의 포인트
- 추천 행동
- 다시 시뮬레이션하기 동선

결과 문구 예시:
- 월세 비중이 높습니다.
- 현재 소비 구조에서는 저축 여력이 낮습니다.
- 희망 지역을 조정하면 가능성이 올라갑니다.
- 비상금 확보 후 독립을 권장합니다.

## Mock 데이터 기준
- Mock 데이터는 UI 흐름 검증용으로만 사용한다.
- Mock 데이터는 실제 계산 로직을 대체하지 않는다.
- Mock 결과 타입은 향후 백엔드 API 응답 타입으로 교체 가능해야 한다.
- Mock 데이터는 feature 내부에 격리하고 UI 컴포넌트에 직접 하드코딩하지 않는다.
- Mock 결과에는 숫자 결과와 해석 메시지를 함께 포함한다.

## UI 상태
- 메인 카드 목록 상태
- 준비 중 카드 상태
- 단계형 입력 초기 상태
- 이전/다음 이동 상태
- 입력 검증 실패 상태
- Mock 결과 생성 중 상태
- 결과 리포트 표시 상태
- 다시 시작 상태

## 성공 조건
- 사용자는 메인 화면에서 시뮬레이션 카드를 보고 서울 자취 가능성 페이지로 이동할 수 있다.
- 서울 자취 가능성 페이지는 단계형 질문 흐름으로 입력을 받는다.
- 잘못된 입력은 제출 전에 안내된다.
- 결과는 생활 분석 리포트처럼 표시된다.
- 프론트엔드는 실제 계산 로직을 구현하지 않는다.
- Mock 데이터는 향후 API 응답으로 교체 가능한 타입 구조를 가진다.
- 모바일과 데스크톱에서 레이아웃이 자연스럽게 동작한다.

## 테스트 조건
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`

테스트에는 최소한 입력 검증, 단계 이동, 결과 리포트 렌더링을 포함한다.

## 변경 파일 목록
예상 변경 파일:
- `frontend/app`
- `frontend/components`
- `frontend/features`
- `frontend/schemas`
- `frontend/types`
- `frontend/lib`

실제 변경 파일:
- `frontend/app/globals.css`
- `frontend/app/layout.tsx`
- `frontend/app/page.tsx`
- `frontend/app/simulations/seoul-living/page.tsx`
- `frontend/components/simulation-card.tsx`
- `frontend/features/simulations/types.ts`
- `frontend/features/simulations/simulation-cards.ts`
- `frontend/features/seoul-living/schema.ts`
- `frontend/features/seoul-living/types.ts`
- `frontend/features/seoul-living/mock-report.ts`
- `frontend/features/seoul-living/seoul-living-simulator.tsx`
- `frontend/features/seoul-living/schema.test.ts`
- `frontend/lib/utils.ts`

## 검증 결과
Status:
- [ ] Not Run
- [x] Passed
- [ ] Failed
- [ ] Skipped

실행 명령:
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`

결과:
- Frontend 검증 4종 모두 통과했다.

생략 사유:
- 해당 없음

## 구현 요약
- 메인 카드형 시뮬레이션 선택 화면을 구현했다.
- 서울 자취 가능성 독립 페이지를 구현했다.
- React Hook Form + Zod 기반 단계형 질문 입력 UX를 구현했다.
- Mock 데이터 기반 생활 분석 리포트 UI를 구현했다.
- Mock 결과 타입과 입력 스키마를 분리해 이후 백엔드 API 응답으로 교체 가능하게 구성했다.
- 서울 자취 입력 스키마 테스트를 추가했다.

## 후속 작업
- `003-backend-simulation-api.md`에서 Mock 결과 타입을 참고해 실제 API 계약과 계산 로직을 확정한다.
- `005-resignation-survival.md`에서 퇴사 후 생존 가능 기간 화면을 구현한다.
- `006-rent-vs-jeonse.md`에서 월세 vs 전세 비교 화면을 구현한다.
- `007-car-affordability.md`에서 자차 유지 가능성 화면을 구현한다.
- `008-subscription-reality.md`에서 청약 현실성 화면을 구현한다.
- `009-government-support.md`에서 정부지원금 또는 보조금 추천 화면을 구현한다.
