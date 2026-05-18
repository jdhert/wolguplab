# AGENTS.md

## Project
월급실험실은 사용자의 연봉, 주거비, 생활비, 소비 습관, 자산/한도 조건을 바탕으로 현실 생활 조건을 시뮬레이션하는 웹 서비스이다.

서비스는 서울 자취 가능성, 퇴사 후 생존 가능 기간, 월세 vs 전세 유불리, 자차 유지 가능성, 청약 현실성 등을 통합적으로 분석한다.

목표는 단순 계산기가 아니라 사용자가 현실적인 의사결정을 할 수 있도록 돕는 해석 중심 플랫폼이다.

## Architecture
- Frontend: Next.js, TypeScript, Tailwind CSS, shadcn/ui, Zod, React Hook Form, TanStack Query
- Backend: Spring Boot, Java 17+, Spring Web, Spring Data JPA, Validation, PostgreSQL
- Infra: Docker, Docker Compose, GitHub Actions, OCI, Nginx Reverse Proxy

## Product Scope
초기 MVP는 서울 자취 가능성 시뮬레이션에 집중한다.

MVP 범위:
- 연봉 기반 실수령 계산
- 지역별 월세/생활비 분석
- 서울 자취 가능성 판단
- 결과 리포트 출력
- 메인 카드형 시뮬레이션 선택 화면
- 서울 자취 가능성 단계형 입력 UX
- Mock 데이터 기반 결과 리포트 UI

후속 확장 범위:
- 퇴사 후 생존 가능 기간
- 월세 vs 전세 비교
- 자차 유지 가능성
- 청약 현실성
- 정부지원금 또는 보조금 추천

## UI Direction
월급실험실은 계산기 사이트처럼 보이면 안 된다.

금지 방향:
- 긴 입력폼 중심 화면
- 엑셀 또는 관리자 페이지 느낌
- 정부 사이트 느낌
- 복잡한 표 중심 대시보드

우선 방향:
- 생활 리포트 느낌
- 질문형, 단계형 입력 UX
- 카드형 시뮬레이션 선택 화면
- 쉬운 해석 중심 결과 리포트
- 토스, 뱅크샐러드, 오늘의집처럼 부담이 낮고 생활 친화적인 금융/생활 UX

초기 프론트엔드는 실제 백엔드 연동보다 UI/UX 흐름 검증을 우선한다. 이 단계에서는 Mock 데이터 기반 결과 리포트를 허용한다.

## Harness Rule
모든 작업은 `docs/plans`의 Plan 문서를 기준으로 진행한다.

Plan은 작업 지시서이자 완료 판정 기준이다. 구현 전에 Plan을 먼저 작성하거나 갱신하고, 구현이 끝나면 실제 변경 내용과 검증 결과를 Plan에 반영한다.

## Multi-Agent Orchestration
OpenCode를 사용할 때도 Plan 문서가 source of truth다.

운영 문서:
- `docs/agent-workflow.md`: OpenCode 멀티 에이전트 운영 규칙
- `docs/agent-board.md`: Plan별 Owner, Reviewer, Write Scope 배정판
- `.opencode/agents/*.md`: 프로젝트 로컬 OpenCode agent 정의

멀티 에이전트 작업 규칙:
- 한 에이전트는 한 번에 하나의 Plan만 담당한다.
- write agent는 지정된 Write Scope 안에서만 수정한다.
- 병렬 write 작업은 Git branch 또는 Git worktree 단위로 분리한다.
- QA 통과 전에는 Plan을 `Done`으로 바꾸지 않는다.
- Integrator가 최종 검증 결과와 구현 요약을 Plan에 기록한다.

## Plan Lifecycle
- Draft: 작업 범위와 성공 조건을 작성하는 단계
- Ready: 구현 가능한 수준으로 범위, 테스트, 변경 파일이 정리된 단계
- In Progress: 해당 Plan 기준으로 구현 중인 단계
- Implemented: 코드 변경은 끝났지만 검증이 남아 있는 단계
- Verified: 필수 검증을 통과한 단계
- Done: 구현 요약과 변경 파일 목록까지 갱신된 완료 단계

## Required Plan Sections
각 Plan 문서는 아래 항목을 포함해야 한다.

- 작업 목표
- 구현 범위
- 제외 범위
- 성공 조건
- 테스트 조건
- 변경 파일 목록
- 검증 결과
- 구현 요약
- Owner
- Reviewer
- Write Scope

필요한 경우 아래 항목을 추가한다.

- API 계약
- 데이터 모델
- UI 상태
- UI/UX 흐름
- Mock 데이터 기준
- 에러 처리
- 보안/개인정보 기준
- 의존 Plan
- Blocked By
- Handoff Notes
- 후속 작업

## Development Rules
- 기능은 반드시 Plan 문서 기준으로 작업한다.
- 작업 전 변경 범위를 먼저 요약한다.
- 기존 구조를 임의로 변경하지 않는다.
- 민감한 사용자 데이터는 기본 저장하지 않는다.
- 계산 로직은 프론트가 아니라 백엔드에 둔다.
- 프론트는 입력, 표시, 클라이언트 검증만 담당한다.
- 백엔드는 시뮬레이션 계산, 데이터 조회, 결과 생성을 담당한다.
- 사용자 입력은 반드시 검증한 뒤 처리한다.
- `any` 타입은 사용하지 않는다.
- 모든 시뮬레이션 결과는 숫자 결과와 해석 메시지를 함께 제공한다.
- 새로운 시뮬레이션 도메인은 기존 Plan에 끼워 넣지 않고 별도 Plan으로 분리한다.
- Mock 데이터는 프론트 UI 흐름 검증용으로만 사용하고, 실제 계산 로직처럼 확장하지 않는다.
- Mock 결과도 실제 API 응답으로 교체 가능한 타입과 구조를 가져야 한다.

## Required Checks
Frontend:
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`

Backend:
- `./gradlew test`
- `./gradlew build`

## Completion Rule
작업 완료는 코드 작성만으로 판단하지 않는다.

완료 조건:
- Plan의 성공 조건을 만족한다.
- 필수 검증을 실행하고 결과를 기록한다.
- 변경 파일 목록을 최신 상태로 갱신한다.
- 구현 요약을 작성한다.
- 실패하거나 생략한 검증이 있다면 이유를 남긴다.
