USE kbhc;
SET NAMES 'utf8mb4';
-- 최초 테이블 생성시 인코딩 깨짐 문제 발생하여 설정

# 1. Table Creation
create table health_measure_infos
(
    measure_id     bigint                                                                                         not null auto_increment comment '측정 ID',
    measure_log_id bigint comment '측정 로그 ID (health_measure_logs)',
    record_key     varchar(255)                                                                                   not null comment '사용자 구분 키',
    measure_type   enum ('BLOOD_PRESSURE', 'BLOOD_SUGAR', 'EXERCISE', 'HEART_RATE', 'OXYGEN_SATURATION', 'STEPS') not null comment '측정 타입 (걸음, 혈압 등)',
    from_date      datetime(6) comment '측정시작일자',
    to_date        datetime(6) comment '측정종료일자',
    measure_detail JSON comment '건강 측정 정보 상세 JSON',
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                                         not null comment '생성일자',
    created_by     VARCHAR(255) DEFAULT 'ANONYMOUS' comment '생성자',
    updated_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                                         not null comment '수정일자',
    updated_by     VARCHAR(255) DEFAULT 'ANONYMOUS' comment '수정자',
    primary key (measure_id)
) comment ='건강 측정 테이블' engine = InnoDB;

create table health_measure_logs
(
    measure_log_id   bigint                                                                             not null auto_increment comment '측정 로그 ID',
    record_key       varchar(255)                                                                       not null comment '사용자 구분 키',
    status           enum ('DONE', 'ERROR', 'PENDING', 'PROCESSING')                                    not null comment '데이터 처리 상태',
    data_source_type enum ('CHECKUP_ANALYSIS', 'HEALTH_APPLE', 'HEALTH_SAMSUNG', 'INBODY', 'UNDEFINED') not null comment '데이터 소스 유형 (삼성 헬스, 애플 건강, ...)',
    raw_json         JSON comment 'JSON 원본 데이터',
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                             not null comment '생성일자',
    created_by       VARCHAR(255) DEFAULT 'ANONYMOUS' comment '생성자',
    updated_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                             not null comment '수정일자',
    updated_by       VARCHAR(255) DEFAULT 'ANONYMOUS' comment '수정자',
    primary key (measure_log_id)
) comment ='건강 측정 로그 테이블' engine = InnoDB;

create table health_measure_summaries
(
    summary_id            bigint                                                                                    not null auto_increment comment '요약 ID',
    record_key            varchar(255)                                                                              not null comment '사용자 구분 키',
    summary_date          date comment '요약일자',
    measure_type          enum ('BLOOD_PRESSURE','BLOOD_SUGAR','EXERCISE','HEART_RATE','OXYGEN_SATURATION','STEPS') not null comment '측정 유형',
    count                 bigint comment '측정 건수',
    sum_steps             bigint                                                                                    not null comment '총 걸음 수 (단위 : 보)',
    sum_calories          float(53)                                                                                 not null comment '총 칼로리 (단위 : kcal)',
    sum_distance          float(53)                                                                                 not null comment '총 거리 (단위 : km)',
    avg_oxygen_saturation float(53)                                                                                 not null comment '평균 산소포화도 (단위 : %)',
    avg_blood_sugar       bigint                                                                                    not null comment '평균 혈당 (단위 : mg/dL)',
    avg_diastolic         bigint                                                                                    not null comment '평균 이완기 혈압 (단위 : mmHg)',
    avg_heart_rate        bigint                                                                                    not null comment '평균 심박수 (단위 : bpm)',
    avg_systolic          bigint                                                                                    not null comment '평균 수축기 혈압 (단위 : mmHg)',
    created_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                                    not null comment '생성일자',
    created_by            VARCHAR(255) DEFAULT 'ANONYMOUS' comment '생성자',
    updated_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP                                                    not null comment '수정일자',
    updated_by            VARCHAR(255) DEFAULT 'ANONYMOUS' comment '수정자',
    primary key (summary_id)
) comment ='건강 측정 요약 테이블' engine = InnoDB;

create table user_profiles
(
    birth_date date comment '생년월일',
    height     float(53) comment '키 (cm)',
    weight     float(53) comment '몸무게 (kg)',
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP not null comment '생성일자',
    profile_id bigint                                 not null auto_increment comment '사용자 프로필 ID',
    updated_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP not null comment '수정일자',
    created_by VARCHAR(255) DEFAULT 'ANONYMOUS' comment '생성자',
    mobile_no  varchar(255) comment '휴대폰번호',
    nickname   varchar(255) comment '닉네임',
    record_key varchar(255)                           not null,
    updated_by VARCHAR(255) DEFAULT 'ANONYMOUS' comment '수정자',
    gender     enum ('MAN', 'WOMAN') comment '성별',
    primary key (profile_id)
) comment ='사용자 프로필 테이블' engine = InnoDB;

create table users
(
    is_active       BOOLEAN      DEFAULT TRUE                          not null comment '활성화여부',
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP             not null comment '생성일자',
    last_login_at   datetime(6) comment '마지막로그인일자',
    updated_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP             not null comment '수정일자',
    username        varchar(100)                                       not null comment '이름',
    created_by      VARCHAR(255) DEFAULT 'ANONYMOUS' comment '생성자',
    email           varchar(255)                                       not null comment '이메일',
    hashed_password varchar(255)                                       not null comment '비밀번호',
    record_key      varchar(255)                                       not null comment '사용자 구분키',
    updated_by      VARCHAR(255) DEFAULT 'ANONYMOUS' comment '수정자',
    role_type       enum ('ROLE_ADMIN', 'ROLE_ANONYMOUS', 'ROLE_USER') not null comment '권한 타입',
    primary key (record_key)
) comment ='사용자 테이블' engine = InnoDB;

alter table user_profiles
    add constraint UK_USER_PROFILE_RECORD_KEY unique (record_key);

alter table users
    add constraint UK_USER_EMAIL unique (email);

alter table health_measure_infos
    add constraint FK_HEALTH_MEASURE_INFO_RECORD_KEY
        foreign key (record_key)
            references users (record_key);

alter table health_measure_logs
    add constraint FK_HEALTH_MEASURE_LOG_RECORD_KEY
        foreign key (record_key)
            references users (record_key);

alter table health_measure_infos
    add constraint UK_HEALTH_MEASURE_INFO_USER_MEASURE_PERIOD unique (record_key, measure_type, from_date, to_date);

alter table health_measure_summaries
    add constraint FK_HEALTH_MEASURE_SUMMARY_RECORD_KEY
        foreign key (record_key)
            references users (record_key);

alter table user_profiles
    add constraint FK_USER_PROFILE_RECORD_KEY
        foreign key (record_key)
            references users (record_key);

# 2. Default Data Setting
insert into users (is_active, created_at, last_login_at, updated_at, username, created_by, email, hashed_password,
                   record_key, updated_by, role_type)
values (1, '2025-12-29 00:40:55', '2025-12-29 00:40:54.524098', '2025-12-29 00:40:55', '관리자', 'ANONYMOUS',
        'admin@admin.com', '$2a$10$4DP/l7oxp5rmu9yauwS1DeowpzzX1qAPJd4sVqJHRZSLuxGBw.WUu',
        '7836887b-b12a-440f-af0f-851546504b13', 'ANONYMOUS', 'ROLE_ADMIN'),
       (1, '2025-12-30 01:16:18', '2025-12-30 01:16:17.823933', '2025-12-30 01:16:18', '관리자3', 'ANONYMOUS',
        'admin3@admin.com', '$2a$10$6bwlweXOPY0MfLrfq..CHeLFl23A3m2iBVoSiDs.F1BRb4nEubb8C',
        '3b87c9a4-f983-4168-8f27-85436447bb57', 'ANONYMOUS', 'ROLE_ADMIN'),
       (1, '2025-12-29 00:42:38', '2025-12-29 00:42:37.496390', '2025-12-29 00:42:38', '관리자1', 'ANONYMOUS',
        'admin1@admin.com', '$2a$10$NgzhfB8UbiilLT2LYfGFsOy02vK2AN3LSOzFDk.fVQlXeHOo8qnWe',
        '7b012e6e-ba2b-49c7-bc2e-473b7b58e72e', 'ANONYMOUS', 'ROLE_ADMIN'),
       (1, '2025-12-30 01:16:10', '2025-12-30 01:16:09.951820', '2025-12-30 01:16:10', '관리자2', 'ANONYMOUS',
        'admin2@admin.com', '$2a$10$uokncc.UEeuzJCRZyC3plOGlkpJy37J6IO91cK0dHy/IuS88pBDA.',
        'e27ba7ef-8bb2-424c-af1d-877e826b7487', 'ANONYMOUS', 'ROLE_ADMIN');

insert into kbhc.user_profiles (birth_date, height, weight, created_at, profile_id, updated_at, created_by, mobile_no,
                                nickname, record_key, updated_by, gender)
values ('1995-08-10', 185, 90, '2025-12-29 00:40:55', 1, '2025-12-29 00:40:55', 'ANONYMOUS', '010-1234-1234', '건강관리자',
        '7836887b-b12a-440f-af0f-851546504b13', 'ANONYMOUS', 'MAN'),
       ('1995-08-10', 185, 90, '2025-12-29 00:42:38', 2, '2025-12-29 00:42:38', 'ANONYMOUS', '010-1234-1234', '건강관리자1',
        '3b87c9a4-f983-4168-8f27-85436447bb57', 'ANONYMOUS', 'MAN'),
       ('1995-08-10', 185, 90, '2025-12-30 01:16:10', 3, '2025-12-30 01:16:10', 'ANONYMOUS', '010-1234-1234', '건강관리자2',
        '7b012e6e-ba2b-49c7-bc2e-473b7b58e72e', 'ANONYMOUS', 'WOMAN'),
       ('1995-08-10', 185, 90, '2025-12-30 01:16:18', 4, '2025-12-30 01:16:18', 'ANONYMOUS', '010-1234-1234', '건강관리자3',
        'e27ba7ef-8bb2-424c-af1d-877e826b7487', 'ANONYMOUS', 'WOMAN');