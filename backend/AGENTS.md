# Backend Agent Rules

## Stack
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Validation
- PostgreSQL

## Rules
- Controller에는 계산 로직을 넣지 않는다.
- 계산 로직은 service 또는 calculator 패키지에 둔다.
- DTO에는 Bean Validation을 적용한다.
- Entity와 Request/Response DTO를 분리한다.
- 사용자 연봉/자산 정보는 기본 저장하지 않는다.
- 저장 기능은 명시적으로 요청된 경우에만 구현한다.
- 공공데이터 수집 로직과 시뮬레이션 로직을 분리한다.
- 시뮬레이션 결과는 계산값과 해석 메시지를 함께 반환한다.
- 서울 자취, 퇴사 생존, 월세 vs 전세, 자차 유지, 청약 분석, 지원금 추천은 각각 독립된 도메인 단위로 분리한다.

## Package
- controller
- service
- calculator
- dto
- domain
- repository
- publicdata
- common
