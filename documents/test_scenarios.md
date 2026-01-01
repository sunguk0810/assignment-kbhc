# 테스트 시나리오 명세서 (Test Scenarios)

## 1. 개요 
이 문서는 프로젝트의 핵심 비즈니스 로직과 데이터 접근 로직을 검증하기 위한 테스트 시나리오를 정의한다.

## 2. 서비스 계층 테스트 시나리오

### 2.1 회원 관리 (UserService)
**테스트 파일 위치**: `src/test/java/com/github/sunguk0810/assignment/domain/auth/service/UserServiceTest.java`

| ID | 테스트 케이스명 | 사전 조건 (Given) | 입력 데이터 (When) | 기대 결과 (Then) |
| :-- | :-- | :-- | :-- | :-- |
| **TS-USER-01** | **회원가입 성공** | 이메일 중복 없음 | 정상적인 회원가입 요청 DTO | 1. `UserRepository.save`가 1회 호출됨<br>2. 반환된 `recordKey`가 null이 아님 |
| **TS-USER-02** | **회원가입 실패 - 이메일 중복** | `findByEmail` 시 이미 존재하는 User 반환 | 기존에 존재하는 이메일로 요청 | 1. `BusinessException` 발생<br>2. 에러 타입이 `EMAIL_DUPLICATION`이어야 함 |

---

### 2.2 인증 관리 (AuthService)
**테스트 파일 위치**: `src/test/java/com/github/sunguk0810/assignment/domain/auth/service/AuthServiceTest.java`

| ID | 테스트 케이스명      | 사전 조건 (Given)                    | 입력 데이터 (When)     | 기대 결과 (Then)                                                                 |
| :-- |:--------------|:---------------------------------|:------------------|:-----------------------------------------------------------------------------|
| **TS-AUTH-01** | **로그인 성공**    | `AuthenticationManager` 인증 성공 처리 | 유효한 이메일/비밀번호      | 1. Access/Refresh 토큰 발급됨<br>2. `User.updateLastLoginAt` 호출됨                  |
| **TS-AUTH-02** | **토큰 재발급 성공** | Redis에 유효한 Refresh Token 존재      | 유효한 Refresh Token | 1. 새로운 Access Token 발급됨<br>2. Refresh Token은 유지됨                             |
| **TS-AUTH-03** | **토큰 재발급 실패** | 회원을 로그아웃 시킨 Refresh Token        | 제거된 Refresh Token | 1. `BusinessException` (REFRESH_TOKEN_NOT_FOUND) 발생                          |
| **TS-AUTH-04** | **로그아웃**      | Redis에 토큰 존재                     | 유효한 Refresh Token | 1. Redis에서 토큰 삭제됨<br>2. `true` 반환                                            |

---

### 2.3 건강 통계 조회 (HealthService)
**테스트 파일 위치**: `src/test/java/com/github/sunguk0810/assignment/domain/health/service/HealthServiceTest.java`

| ID | 테스트 케이스명 | 사전 조건 (Given) | 입력 데이터 (When) | 기대 결과 (Then) |
| :-- | :-- | :-- | :-- | :-- |
| **TS-HLTH-01** | **일간(Daily) 요약 조회** | - | `SummaryType.DAILY`, 기간 설정 | `summaryRepository.findByUser...` 메서드가 호출되어야 함 |
| **TS-HLTH-02** | **월간(Monthly) 요약 조회** | - | `SummaryType.MONTHLY`, 기간 설정 | `summaryRepository.findMonthlyUserSummary` 메서드가 호출되어야 함 |

---

## 3. 리포지토리 계층 테스트 시나리오

### 3.1 건강 요약 리포지토리 (HealthMeasureSummaryRepository)
**테스트 파일 위치**: `src/test/java/com/github/sunguk0810/assignment/domain/health/repository/HealthMeasureSummaryRepositoryTest.java`

| ID | 테스트 케이스명 | 테스트 데이터 셋업 (Fixture) | 검증 로직 (Assertion) |
| :-- | :-- | :-- | :-- |
| **TS-REPO-01** | **월간 요약 집계 (SUM)** | **사용자 A의 걸음 수 데이터:**<br>- 1월 1일: 1000보<br>- 1월 2일: 2000보<br>- 1월 5일: 3000보 | **1월 월간 조회 시:**<br>- 결과 리스트 크기: 1개<br>- `sumSteps`: 6000보 (정확한 합계)<br>- `count`: 3회 |
| **TS-REPO-02** | **월간 요약 집계 (AVG)** | **사용자 A의 혈압 데이터:**<br>- 2월 1일: 수축기 120<br>- 2월 10일: 수축기 130 | **2월 월간 조회 시:**<br>- `avgSystolic`: 125 (정확한 평균)<br>- 소수점 처리 방식 확인 (반올림/버림 등) |
| **TS-REPO-03** | **기간 필터링 검증** | **데이터:**<br>- 1월 31일 (포함)<br>- 2월 15일 (포함)<br>- 3월 1일 (미포함) | **조회 기간: 1월~2월**<br>- 결과에 1월, 2월 데이터만 포함되고 3월 데이터는 제외되어야 함 |
| **TS-REPO-04** | **타 사용자 데이터 격리** | **데이터:**<br>- 사용자 A: 1월 데이터<br>- 사용자 B: 1월 데이터 | **사용자 A로 조회 시:**<br>- 사용자 B의 데이터가 결과에 섞여 나오지 않아야 함 |
