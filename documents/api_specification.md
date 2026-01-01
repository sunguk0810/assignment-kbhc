# API 명세서


## 1. 개요
- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`
- **인증 방식**: `Authorization: Bearer {Access_Token}` (로그인 등 일부 API 제외)

---

## 2. 인증/인가

### 2.1. 회원가입
신규 사용자를 등록합니다.

- **URL**: `/api/v1/auth/register`
- **Method**: `POST`
- **Auth**: None

#### Request
```json
{
    "email": "user@example.com",
    "username": "홍길동",
    "password": "Password123!",
    "profiles": {
        "birthDate": "1990-01-01",
        "gender": "MAN",
        "nickname": "길동이",
        "mobileNo": "010-1234-5678",
        "height": 175.5,
        "weight": 70.0
    }
}
```

#### Response (Success: 200 OK)
```json
{
    "status": "success",
    "message": "회원가입이 성공적으로 완료되었습니다.",
    "data": "uuid-record-key",
    "createdAt": "2025-12-29T09:42:37"
}
```

#### Response (Fail: 400 Bad Request)
- 이메일 중복 시: "이미 존재하는 이메일입니다."
- 유효성 검증 실패 시: "값 검증에 실패하였습니다." (상세 내역 포함)

---

### 2.2. 로그인
이메일과 비밀번호로 인증하고 토큰을 발급받습니다.

- **URL**: `/api/v1/auth/login`
- **Method**: `POST`
- **Auth**: None

#### Request
```json
{
    "email": "user@example.com",
    "password": "Password123!"
}
```

#### Response (Success: 200 OK)
```json
{
    "status": "success",
    "message": "로그인되었습니다.",
    "data": {
        "accessToken": "eyJhbGciOi...",
        "refreshToken": "eyJhbGciOi...",
        "tokenType": "Bearer",
        "expiresIn": 900000
    },
    "createdAt": "2025-12-29T10:31:15"
}
```

---

### 2.3. 토큰 재발급 (Refresh)
만료된 Access Token을 Refresh Token을 통해 갱신합니다.

- **URL**: `/api/v1/auth/refresh-token`
- **Method**: `POST`
- **Auth**: None

#### Request
```json
{
    "refreshToken": "existing_refresh_token_value"
}
```

#### Response (Success: 200 OK)
```json
{
    "status": "success",
    "message": "토큰이 갱신되었습니다.",
    "data": {
        "token": "new_access_token_value",
        "expiresIn": 900000
    },
    "createdAt": "2025-12-29T10:36:04"
}
```

---

### 2.4. 로그아웃
Refresh Token을 무효화하여 로그아웃 처리합니다.

- **URL**: `/api/v1/auth/logout`
- **Method**: `POST`
- **Auth**: Bearer Token

#### Request
```json
{
    "refreshToken": "refresh_token_to_invalidate"
}
```

#### Response (Success: 200 OK)
```json
{
    "status": "success",
    "message": "로그아웃이 완료되었습니다.",
    "data": null,
    "createdAt": "2025-12-29T11:30:29"
}
```

---

## 3. 건강 데이터 (Health)

### 3.1. 건강 측정 정보 저장
웨어러블 기기 등에서 수집된 건강 데이터를 업로드합니다.
(비동기 처리를 위해 서버는 저장 접수만 받고 즉시 응답합니다.)

- **URL**: `/api/v1/health/measure`
- **Method**: `POST`
- **Auth**: Bearer Token

#### Request (Example 1: Steps - 걸음 수)
가장 일반적인 측정 데이터인 걸음 수 전송 예시입니다.
```json
{
    "recordKey": "user-uuid-value",
    "data": {
        "entries": [
            {
                "period": {
                    "from": "2024-11-01 10:40:00",
                    "to": "2024-11-01 10:45:00"
                },
                "steps": 500,
                "distance": {
                    "unit": "meter",
                    "value": 350
                },
                "calories": {
                    "unit": "kcal",
                    "value": 25.5
                }
            }
        ],
        "source": {
            "name": "SAMSUNG_HEALTH"
        }
    },
    "type": "STEPS"
}
```

#### Request (Example 2: Oxygen Saturation - 산소포화도)
참조용 산소포화도 데이터 전송 예시입니다.
```json
{
    "recordkey": "user-uuid-value",
    "data": {
        "entries": [
            {
                "period": {
                    "from": "2024-11-01 10:40:00",
                    "to": "2024-11-01 10:45:00"
                },
                "oxygenSaturation": {
                    "unit": "%",
                    "value": 97
                }
            },
            {
                "period": {
                    "from": "2024-11-01 13:24:00",
                    "to": "2024-11-01 13:29:00"
                },
                "oxygenSaturation": {
                    "unit": "%",
                    "value": 96
                }
            }
        ],
        "source": {
            "name": "Health Kit"
        }
    },
    "type": "OXYGEN_SATURATION"
}
```

#### Response (Success: 200 OK)
```json
{
    "status": "success",
    "message": "측정 데이터가 성공적으로 저장되었습니다.",
    "data": null,
    "createdAt": "2025-12-31T09:00:00"
}
```

---

### 3.2. 건강 데이터 조회
기간별 건강 통계 데이터를 조회합니다.

- **URL**: `/api/v1/health/measure`
- **Method**: `GET`
- **Auth**: Bearer Token

#### Query Parameters
- `startDate`: 조회 시작일 (`YYYY-MM-DD`)
- `endDate`: 조회 종료일 (`YYYY-MM-DD`)
- `type`: 측정 타입 (`STEPS`, `BLOOD_PRESSURE`, `HEART_RATE`, `BLOOD_SUGAR`, `OXYGEN_SATURATION`)
- `summaryType`: 집계 방식 (`DAILY`, `MONTHLY`)

#### Response (Success: 200 OK - Example: Steps)
```json
{
    "status": "success",
    "message": "건강 데이터 조회 성공",
    "data": [
        {
            "summaryDate": "2025-01-01",
            "count": 5,
            "steps": 10500,
            "calories": 350.5,
            "distance": 7.2
        },
        {
            "summaryDate": "2025-01-02",
            "count": 3,
            "steps": 8000,
            "calories": 280.0,
            "distance": 5.5
        }
    ],
    "createdAt": "2025-12-31T09:01:21"
}
```