# 구현 방법 및 설명

본 문서는 프로젝트의 핵심 구현 전략과 개발 과정에서 발생한 주요 이슈 및 해결 방법을 기술합니다.

---

## 1. 프로젝트 구조 및 구현 전략

### 1.1. 도메인형 패키지 구조
기능(Layer) 중심이 아닌 **도메인(Domain) 중심**으로 패키지를 구성했습니다.
- `User`, `Health` 등 도메인별로 관련 코드(Controller, Service, Repository, Entity)를 한곳에 모아 응집도를 높였습니다. 이는 특정 도메인의 비즈니스 로직을 파악하기 쉽게 만들며, 추후 도메인 별로 서비스를 분리할 때 용이합니다.

### 1.2. DTO와 Entity의 철저한 분리
API 요청/응답 객체(DTO)와 데이터베이스 객체(Entity)를 엄격하게 분리했습니다.
- Entity는 DB 스키마와 강하게 결합되어 있어 변경 시 영향도가 큽니다. 반면 API 스펙은 자주 변경될 수 있습니다. 이를 분리함으로써 API 스펙 변경이 DB 설계에 영향을 주지 않도록 하고, 민감한 정보(비밀번호 등)가 API로 노출되는 것을 원천 차단했습니다.

### 1.3. JSON 타입 활용 및 다형성 처리
다양한 건강 데이터(걸음 수, 혈압 등)를 처리하기 위해 MySQL의 **JSON 타입**과 Java의 **다형성** 을 활용했습니다.
- `HealthDetail` 추상 클래스를 두고, 이를 상속받는 `Step`, `BloodPressure` 등을 구현했습니다. DB 저장 시에는 `AttributeConverter`를 통해 Java 객체를 JSON 문자열로 변환하여 `measure_detail` 컬럼에 저장합니다.

---

## 2. 발생한 이슈 및 해결 방법

### 이슈 1: 다양한 형태의 헬스 데이터 수용 문제
- **상황**: 걸음 수는 `count`, `distance`가 필요하고, 혈압은 `systolic`, `diastolic`이 필요하는 등 측정 타입마다 데이터 구조가 상이함. 이를 정규화된 RDB 테이블로 모두 설계하면 테이블 개수가 기하급수적으로 늘어남.
- **해결**:
  - 핵심 검색 조건(사용자, 측정일시, 타입)은 정형 컬럼으로 추출하여 인덱싱.
  - 가변적인 상세 데이터는 **JSON 타입**으로 저장하여 스키마 변경 없이 유연하게 대응 (`Schema-less`의 장점 흡수).
  - JPA `Converter`를 사용하여 애플리케이션 레벨에서는 타입 안전한 객체로 다룰 수 있도록 구현.

### 이슈 2: 대용량 데이터 조회 성능 저하 우려
- **상황**: 수백만 건의 측정 로그가 쌓여있는 상태에서 사용자가 "이번 달 걸음 수 통계"를 요청할 경우, 매번 `SUM()`이나 `AVG()` 쿼리를 실행하면 DB 부하가 심하고 응답 속도가 느려짐.
- **해결 (CQRS 패턴 적용)**:
  - **쓰기 모델(Command)**: 원본 로그는 `HealthMeasureLog`에 빠르게 적재.
  - **읽기 모델(Query)**: 데이터 수신 시점(Kafka Consumer)에 비동기로 통계 데이터를 미리 계산(Pre-aggregation)하여 `HealthMeasureSummary` 테이블에 저장.
  - 조회 API는 별도의 연산 없이 `HealthMeasureSummary` 테이블만 단순 조회하므로, 데이터 양이 늘어나도 **O(1)**에 가까운 일정한 응답 속도 보장.

### 이슈 3: 중복 데이터 유입 처리 (Idempotency)
- **상황**: 네트워크 불안정이나 클라이언트 로직 오류로 인해 동일한 측정 데이터가 중복 전송될 가능성 존재.
- **해결**:
  - `HealthMeasureInfo` 테이블에 `(record_key, measure_type, from_date, to_date)` 조합으로 **Unique Index** 설정.
  - 데이터 저장 시 `INSERT IGNORE` (또는 `Upsert`) 로직을 사용하여, 중복 데이터가 들어오더라도 에러를 발생시키지 않고 자연스럽게 무시하거나 갱신되도록 처리하여 데이터 무결성 유지.


---

## 5. 추가 이슈 해결 사례 및 기술적 고민 (Technical Challenges)

개발 과정에서 마주친 구체적인 기술적 문제들과 그 해결 과정을 정리했습니다.

### 5.1. JPA 통계 쿼리 매핑 오류 (`Column 'summary_id' not found`)
- **상황**: 월간 요약 데이터를 `GROUP BY`로 조회하여 `HealthMeasureSummary` 엔티티로 반환하려 했으나, 집계 결과에는 식별자(`summary_id`)가 없어 JPA가 엔티티를 매핑하지 못함.
- **해결**: 영속성 컨텍스트를 거치지 않는 **`JdbcTemplate`**을 도입. SQL 실행 후 `RowMapper`를 통해 비영속(Transient) 엔티티 객체를 직접 생성하여 반환함으로써 해결.

### 5.2. 측정 타입별 동적 쿼리 구성 전략
- **상황**: 측정 타입(`STEPS`, `BLOOD_PRESSURE` 등)에 따라 조회해야 할 컬럼(`SUM(steps)` vs `AVG(systolic)`)이 달라져 정적 쿼리(`@Query`)로 처리 불가.
- **해결**: Java 코드 레벨에서 `switch` 문을 활용해 **쿼리 조각(Fragment)**을 생성하고, `String.format`으로 최종 SQL을 조립하는 동적 SQL 방식 적용.

### 5.3. 비동기 스레드(Kafka Consumer)에서의 JPA Auditing
- **상황**: Kafka Consumer가 데이터를 저장할 때, `SecurityContext`가 비어있어 `@CreatedBy`가 `ANONYMOUS`로 기록됨.
- **해결**: Consumer 로직 내에서 이벤트에 포함된 사용자 정보를 기반으로 임시 `Authentication` 객체를 생성하여 `SecurityContext`에 주입 후 저장 로직 수행.

---

## 6. 주요 공통 모듈 설명 (Common Modules)

### 6.1. `TokenProvider` (JWT 보안)
- **역할**: JWT 토큰의 생성, 검증, 파싱을 담당하는 핵심 보안 컴포넌트.
- **특징**: 
  - `Access Token`과 `Refresh Token`의 생명주기를 분리 관리.
  - 토큰 파싱 시 `CustomUserDetails` 객체를 복원하여 `SecurityContext`에 인증 정보를 제공.

### 6.2. `BaseEntity` (JPA Auditing)
- **역할**: 모든 엔티티의 공통 부모 클래스로, 데이터의 생성/수정 이력을 자동 관리.
- **구성**: 
  - `createdAt`, `updatedAt`: 생성 및 수정 시간 자동 기록 (`@CreatedDate`, `@LastModifiedDate`).
  - `createdBy`, `updatedBy`: 생성 및 수정 주체(사용자 ID) 자동 기록 (`@CreatedBy`, `@LastModifiedBy`).
- **장점**: 반복적인 감사(Audit) 코드를 제거하고 데이터 무결성 확보.

### 6.3. `GlobalExceptionHandler` (전역 예외 처리)
- **역할**: 애플리케이션 전역에서 발생하는 예외를 가로채어 표준화된 JSON 응답(`ApiResponse`)으로 변환.
- **처리 범위**: 
  - `BusinessException`: 의도된 비즈니스 로직 에러.
  - `MethodArgumentNotValidException`: `@Valid` 검증 실패 시 필드별 상세 에러 메시지 반환.
  - `Exception`: 예측하지 못한 서버 내부 오류(500)에 대한 안전망.


