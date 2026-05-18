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
- `003-backend-simulation-api.md`

Handoff Notes:
- Backend API 구현 이후 로컬 통합 실행 환경을 구성한다.

## 작업 목표
프론트엔드, 백엔드, 데이터베이스를 로컬에서 한 번에 실행할 수 있는 Docker Compose 환경을 구성한다.

## 구현 범위
- 루트 `docker-compose.yml` 작성
- 프론트엔드 Dockerfile 작성
- 백엔드 Dockerfile 작성
- 데이터베이스 서비스 구성
- 환경 변수 예시 파일 작성
- 로컬 실행 방법 문서화

## 제외 범위
- 운영 서버 배포
- OCI 인프라 구성
- Nginx Reverse Proxy 운영 설정
- GitHub Actions 배포 자동화

## 성공 조건
- `docker compose up --build`로 로컬 전체 서비스를 실행할 수 있다.
- 프론트엔드에서 백엔드 API에 접근할 수 있다.
- 백엔드는 데이터베이스에 연결할 수 있다.
- 민감한 환경 변수는 저장소에 직접 커밋하지 않는다.

## 테스트 조건
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
- GitHub Actions 기반 CI 검증을 별도 Plan으로 분리한다.
- OCI/Nginx 운영 배포 설정을 별도 Plan으로 분리한다.
