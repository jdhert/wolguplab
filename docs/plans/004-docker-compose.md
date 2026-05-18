# 004 Docker Compose

Status: Draft

Depends on:
- `001-project-setup.md`
- `002-frontend-simulation-form.md`
- `003-backend-simulation-api.md`

Owner:
- Infra Worker

Reviewer:
- Integrator

Write Scope:
- `docker-compose.yml`
- `frontend/Dockerfile`
- `backend/Dockerfile`
- `.env.example`

Blocked By:
- `003-backend-simulation-api.md` implementation and verification
- JDK 17 이상 backend 실행 환경 확인
- backend startup behavior and smoke-test endpoint decision
- Docker service boundaries and environment variable wiring decision

Handoff Notes:
- 이 Plan은 아직 `Draft`다.
- Docker 구현은 Plan 003 backend API가 구현되고 최소 smoke-test 가능한 상태가 된 뒤 시작한다.
- 지금 단계에서는 blocker와 환경 변수 정책만 명확히 한다.
- 실제 API key, DB password, local personal value는 커밋하지 않는다.

## 작업 목표
프론트엔드, 백엔드, 데이터베이스를 로컬에서 한 번에 실행할 수 있는 Docker Compose 환경을 구성한다.

단, 현재는 backend API와 JDK 17 환경이 준비되기 전이므로 Docker 구현을 시작하지 않는다.

## 현재 Draft 판단
Plan 004는 다음 이유로 아직 `Ready`가 아니다.

- Plan 003 backend API가 아직 구현되지 않았다.
- backend 검증은 JDK 17 이상 환경이 준비되어야 한다.
- Compose smoke test에 사용할 backend endpoint가 아직 확정되지 않았다.
- backend와 database 연결 방식은 구현 이후 확인이 필요하다.
- `.env.example`은 placeholder-only로 준비되지만 실제 secret은 별도 로컬 파일에서만 관리해야 한다.

## 구현 범위
Ready 이후 예상 구현 범위:
- 루트 `docker-compose.yml` 작성
- 프론트엔드 Dockerfile 작성
- 백엔드 Dockerfile 작성
- 데이터베이스 서비스 구성
- 환경 변수 예시 파일 유지
- 로컬 실행 방법 문서화

## 제외 범위
- 운영 서버 배포
- OCI 인프라 구성
- Nginx Reverse Proxy 운영 설정
- GitHub Actions 배포 자동화
- 실제 API key 또는 DB password 작성
- backend API 구현
- frontend UI 변경

## Environment Variable Policy
Repository에 커밋 가능한 파일:
- `.env.example`

Repository에 커밋하면 안 되는 파일:
- `.env`
- `.env.local`
- `.env.*.local`

Rules:
- `.env.example`은 template only다.
- `.env.example`에는 빈 값 또는 placeholder만 둔다.
- local 개발자는 실제 값을 `.env` 또는 `.env.local`에 둔다.
- frontend browser runtime에 노출되는 값만 `NEXT_PUBLIC_` prefix를 사용한다.
- backend/database secret에는 `NEXT_PUBLIC_` prefix를 사용하지 않는다.
- `NEXT_PUBLIC_KAKAO_MAP_APP_KEY`는 browser-exposed key지만 실제 값을 repo에 커밋하지 않는다.
- `DATABASE_PASSWORD`는 secret이므로 repo template에서는 비워둔다.

Expected variables:

| Variable | Purpose | Owner | Consumed by | Repository policy |
|---|---|---|---|---|
| `NEXT_PUBLIC_KAKAO_MAP_APP_KEY` | Kakao Maps JavaScript SDK browser key | Frontend/Infra | Frontend browser runtime | blank placeholder only |
| `DATABASE_URL` | Backend PostgreSQL JDBC URL | Backend/Infra | Spring Boot datasource, Docker Compose | blank placeholder only |
| `DATABASE_USERNAME` | Backend DB username | Backend/Infra | Spring Boot datasource, Docker Compose | blank placeholder only |
| `DATABASE_PASSWORD` | Backend DB password | Backend/Infra | Spring Boot datasource, Docker Compose | secret, blank placeholder only |

## 성공 조건
Ready 이후 구현 시:
- `docker compose up --build`로 로컬 전체 서비스를 실행할 수 있다.
- 프론트엔드에서 백엔드 API에 접근할 수 있다.
- 백엔드는 데이터베이스에 연결할 수 있다.
- 민감한 환경 변수는 저장소에 직접 커밋하지 않는다.
- `.env.example`은 placeholder-only 정책을 유지한다.

## 테스트 조건
Ready 이후 구현 시:
- `docker compose config`
- `docker compose up --build`
- 프론트엔드 접속 확인
- 백엔드 헬스 체크 또는 기본 API 확인

## 변경 파일 목록
예상 변경 파일:
- `docker-compose.yml`
- `frontend/Dockerfile`
- `backend/Dockerfile`
- `.env.example`

실제 변경 파일:
- 구현 후 실제 변경 파일 목록으로 갱신한다.

## 검증 결과
Status:
- [x] Not Run
- [ ] Passed
- [ ] Failed
- [ ] Skipped

실행 명령:
- 

결과:
- 

생략 사유:
- 아직 Draft 상태이며 Docker 구현 전이다.

## 구현 요약
아직 구현하지 않았다.

## 후속 작업
- Plan 003 backend API 구현과 검증 이후 Docker Compose 구성을 Ready로 승격할지 판단한다.
- GitHub Actions 기반 CI 검증을 별도 Plan으로 분리한다.
- OCI/Nginx 운영 배포 설정을 별도 Plan으로 분리한다.
