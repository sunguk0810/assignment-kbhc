# Assignment - 건강 데이터 수집 및 분석 서비스

본 프로젝트는 대용량 건강 데이터를 수집, 처리, 조회하기 위한 백엔드 서비스입니다.
다양한 웨어러블 기기(Samsung Health, Apple Health 등)로부터 수집된 비정형 데이터를 효율적으로 저장하고, 사용자에게 일별/월별 건강 통계를 제공하는 것을 목표로 합니다.

---

## 프로젝트 문서

자세한 내용은 아래 문서들을 참고해 주세요.

*   **[요구사항 명세서 (Requirements)](documents/requirement_detail.md)**: 프로젝트의 기능 및 비기능 요구사항 정의
*   **[아키텍처 설계서 (Architecture)](documents/architecture_design.md)**: 시스템 구성도, 데이터 파이프라인 흐름, 패키지 구조
*   **[데이터베이스 설계서 (DB Schema)](documents/db_schema.md)**: 테이블 명세, ERD, 설계 의도
*   **[API 명세서 (API Spec)](documents/api_specification.md)**: REST API 엔드포인트 및 요청/응답 예시
*   **[구현 상세 및 트러블슈팅 (Implementation)](documents/implementation_details.md)**: 핵심 구현 전략, 주요 이슈 해결 과정 및 데이터 필드 상세
*   **[데이터베이스 조회 결과 보고서 (Query Report)](documents/database_query_results.md)**: 요구사항에 따른 SQL 쿼리 실행 결과 및 분석
*   **[테스트 시나리오 (Test Scenarios)](documents/test_scenarios.md)**: 핵심 비즈니스 로직 검증을 위한 테스트 케이스 정의 및 절차

---

## 주요 기능

1.  **사용자 인증/인가 (Authentication)**
    *   JWT 기반 로그인 및 회원가입
    *   Access/Refresh Token을 이용한 보안 세션 관리
    *   BCrypt 비밀번호 암호화
2.  **건강 데이터 수집 (Data Collection)**
    *   **JSON 기반 유연한 저장소**: 다양한 기기의 데이터 포맷 수용
    *   **비동기 처리 (Kafka)**: 대량의 데이터 유입 시에도 빠른 응답 보장
    *   **데이터 추적성**: 원본 로그(Raw Log) 보존 및 상세 데이터 파싱
3.  **건강 통계 조회 (Data Analysis)**
    *   **CQRS 패턴 적용**: 읽기 모델(`Summary`)과 쓰기 모델 분리
    *   **Pre-aggregation**: 데이터 수집 시점에 실시간 통계 생성으로 조회 성능 최적화
    *   일별/월별 요약 데이터 제공

---

## 기술 스택 (Tech Stack)

| Category | Technology | Version          |
| :--- | :--- |:-----------------|
| **Language** | Java | 17               |
| **Framework** | Spring Boot | 4.0.1            |
| **Database** | MySQL | 8.0              |
| **Cache** | Redis | 7.x              |
| **Message Queue** | Apache Kafka | 3.x              |
| **Build Tool** | Gradle | 9.x (Kotlin DSL) |
| **Container** | Docker / Docker Compose | -                |

---

## 시작하기

### 사전 준비사항
*   Java 17 이상
*   Docker & Docker Compose

### 1. 프로젝트 클론
```bash
git clone https://github.com/sunguk0810/assignment-kbhc.git
cd assignment-kbhc
```

### 2. 인프라 실행 (MySQL, Redis, Kafka)
프로젝트 루트에서 Docker Compose를 실행하여 필요한 미들웨어를 띄웁니다.
```bash
docker-compose up -d
```
*   **MySQL**: `localhost:13306`
*   **Redis**: `localhost:16379`
*   **Kafka**: `localhost:9092` (Internal: `29092`)
*   **Kafka UI**: `http://localhost:18080` (토픽 모니터링용)

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```
애플리케이션이 정상적으로 실행되면 `http://localhost:8080`으로 접근 가능합니다.

### 4. API 테스트 및 문서 확인
본 프로젝트는 **Swagger UI**와 **Postman Collection** 두 가지 방식을 모두 지원합니다.

#### 1) Swagger UI (웹 브라우저)
서버 실행 후 아래 URL에 접속하여 API 문서를 확인하고 직접 테스트해볼 수 있습니다.
- **URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **인증 방법**:
    1. `POST /api/v1/auth/login` 또는 `register` API를 통해 `accessToken` 발급
    2. 우측 상단의 **Authorize** 버튼 클릭
    3. Value 란에 토큰 값 입력
    4. **Authorize** -> **Close**

#### 2) Postman (API 클라이언트)
`documents/postman/Postman_API_Collection.json` 파일을 Postman에 Import하여 미리 구성된 요청 예시를 바로 사용할 수 있습니다.

---
