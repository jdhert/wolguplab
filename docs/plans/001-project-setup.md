# 001 Project Setup

Status: Implemented

Started at: 2026-05-18

Depends on:
- `000-plan-template.md`

Owner:
- Integrator

Reviewer:
- QA Reviewer

Write Scope:
- `frontend/*`
- `backend/*`
- root config

Blocked By:
- JDK 17 이상 로컬 환경 구성

Handoff Notes:
- Frontend 검증은 통과했다.
- Backend 검증은 현재 Java 8 환경 때문에 실패한다.

## 작업 목표
월급실험실 MVP 개발을 시작할 수 있도록 프로젝트 기본 구조, 실행 스크립트, Harness 규칙을 정리한다.

## 구현 범위
- 루트 프로젝트 문서와 Plan 기반 작업 흐름 정리
- `frontend` 기본 Next.js 프로젝트 구성
- `backend` 기본 Spring Boot 프로젝트 구성
- 프론트엔드/백엔드 필수 검증 명령 준비
- 공통 개발 규칙을 `AGENTS.md`에 명시
- 프론트엔드 전용 규칙을 `frontend/AGENTS.md`에 명시
- 백엔드 전용 규칙을 `backend/AGENTS.md`에 명시

## 제외 범위
- 실제 시뮬레이션 계산 로직
- 실제 공공데이터 연동
- 사용자 데이터 저장 기능
- 운영 배포 설정

## 성공 조건
- 루트, 프론트엔드, 백엔드 디렉터리 역할이 명확하다.
- 프론트엔드와 백엔드를 독립적으로 실행하거나 검증할 수 있다.
- 이후 작업은 `docs/plans`의 Plan 문서 기준으로 진행할 수 있다.
- 하위 `AGENTS.md`는 루트 규칙을 반복하지 않고 영역별 규칙만 보강한다.
- 기본 프로젝트 생성 후 필수 검증 명령이 실행 가능한 상태다.

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
- `AGENTS.md`
- `docs/plans/000-plan-template.md`
- `frontend/AGENTS.md`
- `frontend/package.json`
- `backend/AGENTS.md`
- `backend/build.gradle`

실제 변경 파일:
- `.gitignore`
- `frontend/package.json`
- `frontend/package-lock.json`
- `frontend/next.config.ts`
- `frontend/tsconfig.json`
- `frontend/next-env.d.ts`
- `frontend/postcss.config.js`
- `frontend/tailwind.config.ts`
- `frontend/.eslintrc.json`
- `frontend/vitest.config.ts`
- `backend/.gitattributes`
- `backend/.gitignore`
- `backend/build.gradle`
- `backend/settings.gradle`
- `backend/gradlew`
- `backend/gradlew.bat`
- `backend/gradle/wrapper/gradle-wrapper.jar`
- `backend/gradle/wrapper/gradle-wrapper.properties`
- `backend/src/main/java/com/wolguplab/backend/BackendApplication.java`
- `backend/src/main/resources/application.properties`
- `backend/src/test/java/com/wolguplab/backend/BackendApplicationTests.java`
- `backend/src/test/resources/application-test.properties`

## 검증 결과
Status:
- [ ] Not Run
- [ ] Passed
- [x] Failed
- [ ] Skipped

실행 명령:
- `npm run lint`
- `npm run type-check`
- `npm run test`
- `npm run build`
- `.\gradlew.bat test`
- `.\gradlew.bat build`

결과:
- Frontend 검증 4종은 모두 통과했다.
- Backend는 Gradle 실행 단계에서 실패했다.
- 실패 메시지: `Gradle requires JVM 17 or later to run. Your build is currently configured to use JVM 8.`

생략 사유:
- `.\gradlew.bat build`는 `.\gradlew.bat test`가 JVM 버전 문제로 실패하여 실행하지 못했다.
- 현재 `JAVA_HOME`이 `D:\synap\JDK18`로 잡혀 있지만 실제 버전은 Java 8이다.

## 구현 요약
- Frontend는 Next.js, TypeScript, Tailwind CSS, React Hook Form, Zod, Vitest 기반으로 구성했다.
- Backend는 Spring Initializr 기반 Gradle/Spring Boot 프로젝트로 구성했다.
- Backend 테스트용 H2 설정을 추가하고, 기본 런타임 DB는 PostgreSQL 환경 변수 기반으로 설정했다.
- 로컬 검증을 위해서는 JDK 17 이상으로 `JAVA_HOME`을 교체해야 한다.

## 후속 작업
- `002-frontend-simulation-form.md`에서 Mock 데이터 기반 메인 선택 화면, 서울 자취 가능성 단계형 입력 UX, 결과 리포트 UI를 먼저 구현한다.
- `003-backend-simulation-api.md`에서 프론트 Mock 타입을 참고해 API 계약과 계산 로직을 확정한다.
- `004-docker-compose.md`에서 전체 로컬 실행 환경을 구성한다.
