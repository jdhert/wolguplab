# Agent Workflow

이 문서는 OpenCode를 실행 런타임으로 사용해 월급실험실 Harness를 멀티 에이전트 방식으로 운용하는 규칙이다.

## 기본 원칙
- `docs/plans/*.md`가 작업의 기준이다.
- 한 에이전트는 한 번에 하나의 Plan만 담당한다.
- write agent는 반드시 지정된 Write Scope 안에서만 수정한다.
- 여러 write agent가 같은 working tree에서 동시에 작업하지 않는다.
- 병렬 작업은 Git branch 또는 Git worktree 단위로 분리한다.
- QA 통과 전에는 Plan을 `Done`으로 올리지 않는다.
- Integrator가 아닌 에이전트는 임의로 다른 Plan의 상태를 완료 처리하지 않는다.

## 역할
Orchestrator:
- 작업 우선순위를 정한다.
- Plan을 에이전트에게 배정한다.
- 충돌 가능성이 있는 작업을 동시에 배정하지 않는다.

Planner Agent:
- Plan을 `Draft`에서 `Ready`로 끌어올린다.
- 목표, 범위, 성공 조건, 테스트 조건, Write Scope를 구체화한다.
- 코드 수정은 하지 않는다.

Frontend Worker:
- 프론트엔드 Plan을 구현한다.
- `frontend/AGENTS.md`와 해당 Plan의 Write Scope를 따른다.
- 계산 로직을 프론트에 추가하지 않는다.

Backend Worker:
- 백엔드 Plan을 구현한다.
- `backend/AGENTS.md`와 해당 Plan의 Write Scope를 따른다.
- Controller, Service, Calculator, DTO 책임을 분리한다.

Infra Worker:
- Docker, Compose, CI, 환경 변수, 배포 관련 Plan을 구현한다.
- 민감한 값은 커밋하지 않는다.

QA Reviewer:
- Plan 성공 조건 기준으로 검토한다.
- 버그, 회귀, 누락된 테스트, Harness 위반을 찾는다.
- 기본적으로 파일 수정 없이 리뷰 결과를 남긴다.

Integrator:
- 병렬 작업 결과를 병합한다.
- 충돌을 정리한다.
- 최종 검증을 실행한다.
- 검증 결과와 구현 요약을 Plan에 반영한다.

## Plan 상태 전환
- `Draft`: 아이디어 또는 초안 상태
- `Ready`: 구현 가능한 수준으로 범위와 검증 조건이 정리된 상태
- `In Progress`: 특정 Owner가 작업 중인 상태
- `Implemented`: 코드 변경은 끝났지만 검증 또는 통합이 남은 상태
- `Verified`: 필수 검증을 통과한 상태
- `Done`: 검증 결과, 변경 파일 목록, 구현 요약까지 기록된 상태

상태 전환 규칙:
- `Draft -> Ready`: Planner 또는 Orchestrator가 수행한다.
- `Ready -> In Progress`: Owner 배정 후 수행한다.
- `In Progress -> Implemented`: Worker가 변경을 끝낸 뒤 수행한다.
- `Implemented -> Verified`: QA 또는 Integrator가 검증 후 수행한다.
- `Verified -> Done`: Integrator가 최종 기록을 확인한 뒤 수행한다.

## Git Worktree 운영
write agent를 병렬로 돌릴 때는 worktree를 권장한다.

예시:

```bash
git worktree add ../wolguplab-003 -b feat/003-backend-api
git worktree add ../wolguplab-004 -b feat/004-docker-compose
```

각 worktree에서 OpenCode를 실행한다.

```bash
cd ../wolguplab-003
opencode
```

작업 요청 예시:

```text
@backend-worker docs/plans/003-backend-simulation-api.md 기준으로 작업해줘.
Write Scope는 backend/src/main/java, backend/src/test/java, backend/build.gradle만 사용해.
작업 후 테스트 결과와 변경 파일을 Plan에 기록해.
```

같은 working tree에서 동시에 허용되는 조합:
- 하나의 write worker + 하나 이상의 read-only reviewer
- Planner + QA reviewer

같은 working tree에서 동시에 금지되는 조합:
- Frontend Worker + Backend Worker가 문서나 공통 파일을 함께 수정
- 두 worker가 같은 Plan 또는 같은 파일 범위를 수정
- Integrator가 병합 중일 때 worker가 추가 수정

## Handoff 기준
Worker는 작업 종료 시 아래를 남긴다.

- 변경 파일 목록
- 실행한 검증 명령
- 검증 결과
- 실패하거나 생략한 검증 사유
- 후속 작업 또는 남은 리스크

QA Reviewer는 아래를 남긴다.

- 통과 여부
- 발견한 문제
- 재현 방법
- 필요한 추가 테스트
- Plan 상태 변경 가능 여부

Integrator는 아래를 확인한다.

- Plan 성공 조건 충족 여부
- 변경 파일 목록 최신화 여부
- 검증 결과 기록 여부
- 충돌 또는 범위 초과 수정 여부
- 후속 Plan 연결 여부

## OpenCode 사용 규칙
- OpenCode는 에이전트 실행기이고, Plan 문서가 작업의 source of truth다.
- OpenCode의 Plan agent는 분석과 계획에 사용한다.
- OpenCode의 Build 또는 Worker agent는 구현에 사용한다.
- `@qa-reviewer`는 검토 전용으로 사용한다.
- `@integrator`는 병합과 최종 기록 정리에 사용한다.

## 금지 사항
- Plan 없이 기능을 구현하지 않는다.
- Owner가 없는 Plan을 구현하지 않는다.
- Write Scope 밖 파일을 수정하지 않는다.
- 검증 없이 Plan을 `Done`으로 바꾸지 않는다.
- 민감한 API key, DB password, 개인 정보를 커밋하지 않는다.
- Mock 데이터를 실제 계산 로직처럼 확장하지 않는다.
