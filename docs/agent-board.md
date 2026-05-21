# Agent Board

OpenCode 멀티 에이전트 운영을 위한 작업 배정판이다.

| Plan | Status | Owner | Reviewer | Write Scope | Notes |
|---|---|---|---|---|---|
| `000-plan-template.md` | Draft | Planner | Integrator | `docs/plans/000-plan-template.md` | 공통 Plan 템플릿 |
| `001-project-setup.md` | Implemented | Integrator | QA Reviewer | `frontend/*`, `backend/*`, root config | Frontend 검증 통과, Backend는 JDK 17 필요 |
| `002-frontend-simulation-form.md` | Done | Frontend Worker | QA Reviewer | `frontend/app`, `frontend/components`, `frontend/features`, `frontend/lib` | Mock UI MVP 완료 |
| `003-backend-simulation-api.md` | Verified | Backend Worker | QA Reviewer | `backend/src/main/java`, `backend/src/test/java`, `backend/build.gradle` | PR merge 후 master에서 backend clean test build 통과 |
| `004-docker-compose.md` | Draft | Infra Worker | Integrator | `docker-compose.yml`, `frontend/Dockerfile`, `backend/Dockerfile`, `.env.example` | 003 구현 및 JDK 17 이후 통합; .env.example placeholder 필요 |
| `005-resignation-survival.md` | Draft | Planner | QA Reviewer | `docs/plans/005-resignation-survival.md` | 후속 도메인 |
| `006-rent-vs-jeonse.md` | Draft | Planner | QA Reviewer | `docs/plans/006-rent-vs-jeonse.md`, future frontend/backend scope | Kakao Maps 기준 명시됨 |
| `007-car-affordability.md` | Draft | Planner | QA Reviewer | `docs/plans/007-car-affordability.md` | 후속 도메인 |
| `008-subscription-reality.md` | Draft | Planner | QA Reviewer | `docs/plans/008-subscription-reality.md` | 후속 도메인 |
| `009-government-support.md` | Draft | Planner | QA Reviewer | `docs/plans/009-government-support.md` | 후속 도메인 |

## 운영 규칙
- `Owner`가 실제 구현 책임자다.
- `Reviewer`는 성공 조건과 검증 결과를 확인한다.
- `Write Scope` 밖 수정이 필요하면 Plan을 먼저 갱신한다.
- `Status`는 Plan 파일의 상태와 항상 일치해야 한다.
- 병렬 작업은 서로 다른 Write Scope일 때만 허용한다.
- `Done`은 QA 또는 Integrator 검증 이후에만 사용할 수 있다.

## 다음 추천 작업
1. JDK 17 이상을 설치/사용하고 `java -version`, `./gradlew test`로 backend 실행 환경을 확인한다.
2. `003-backend-simulation-api.md` 최종 기록을 확인한 뒤 `Done` 승격 여부를 판단한다.
3. `004-docker-compose.md`는 Plan 003 검증 결과를 기준으로 `Ready` 승격 여부를 판단한다.

## Team Mode 실행
- Runbook: `docs/oh-my-opencode-runbook.md`
- Team spec: `.omo/teams/wolguplab-mvp/config.json`
- Project config: `.opencode/oh-my-openagent.jsonc`
