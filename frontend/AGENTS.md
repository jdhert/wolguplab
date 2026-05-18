# Frontend Agent Rules

## Stack
- Next.js
- TypeScript
- Tailwind CSS
- shadcn/ui
- React Hook Form
- Zod
- TanStack Query

## Rules
- 모든 컴포넌트는 TypeScript로 작성한다.
- any 타입 사용 금지.
- 사용자 입력 폼은 React Hook Form + Zod로 검증한다.
- API 응답 타입은 명시적으로 정의한다.
- 계산 로직을 프론트에 두지 않는다.
- app router 구조를 사용한다.
- Server Component를 기본으로 사용하고, 입력 폼만 Client Component로 분리한다.

## UX Direction
- 계산기 사이트처럼 보이는 긴 입력폼을 만들지 않는다.
- 메인 페이지는 탭보다 카드형 시뮬레이션 선택 구조를 우선한다.
- 각 시뮬레이션은 독립 라우트로 구성한다.
- 입력은 단계형 또는 질문형 UX로 구성한다.
- 결과는 숫자 나열이 아니라 생활 분석 리포트처럼 보여준다.
- 관리자 페이지, ERP, 통계 대시보드, 공공기관 사이트 느낌을 피한다.
- 토스, 뱅크샐러드, 오늘의집처럼 쉽고 생활 친화적인 톤을 지향한다.

## Routes
- `/`: 카드형 시뮬레이션 선택 화면
- `/simulations/seoul-living`: 서울 자취 가능성 시뮬레이션
- `/simulations/resignation`: 퇴사 생존 분석
- `/simulations/car`: 자차 유지 가능성
- `/simulations/rent-vs-jeonse`: 월세 vs 전세 비교
- `/simulations/subscription`: 청약 현실성 분석
- `/simulations/government-support`: 지원금 추천

## Mock UI Rules
- 초기 MVP UI는 Mock 데이터 기반 결과 리포트를 허용한다.
- Mock 데이터는 UI 흐름 확인용이며 실제 계산 로직을 대체하지 않는다.
- Mock 응답 타입은 향후 백엔드 API 응답 타입으로 교체 가능해야 한다.
- Mock 데이터는 feature 내부에 격리하고 컴포넌트에 직접 하드코딩하지 않는다.

## Directory
- app/: route
- components/: UI components
- features/: domain feature
- lib/: utilities
- schemas/: zod schemas
- types/: shared frontend types
