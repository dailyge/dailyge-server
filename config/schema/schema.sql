CREATE TABLE IF NOT EXISTS code_and_message
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT 'ID',
    domain           VARCHAR(255)                      NOT NULL COMMENT '도메인',
    name             VARCHAR(255)                      NOT NULL COMMENT '이름',
    code             INT                               NOT NULL COMMENT '코드',
    message          VARCHAR(255)                      NOT NULL COMMENT '메시지',
    created_at       TIMESTAMP                         NOT NULL COMMENT '생성 일시',
    created_by       BIGINT                            NULL COMMENT '생성자',
    last_modified_at TIMESTAMP                         NULL COMMENT '최종 수정 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정자',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '코드 & 메시지';

DROP TABLE IF EXISTS user_sequence;
CREATE TABLE IF NOT EXISTS user_sequence
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '사용자 PK',
    email    VARCHAR(50)                       NOT NULL COMMENT '이메일',
    executed BIT                               NOT NULL DEFAULT FALSE COMMENT '처리 여부'
) ENGINE InnoDB COMMENT '사용자 채번 테이블';

DROP TABLE IF EXISTS tasks;
CREATE TABLE IF NOT EXISTS tasks
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY    NOT NULL COMMENT '할 일 ID',
    user_id          BIGINT                               NOT NULL COMMENT '사용자 ID',
    title            VARCHAR(255)                         NOT NULL COMMENT '제목',
    content          VARCHAR(255)                         NOT NULL COMMENT '내용',
    `month`          INT                                  NOT NULL COMMENT '월',
    `year`           INT                                  NOT NULL COMMENT '년',
    `date`           DATE                                 NOT NULL COMMENT '날짜',
    status           ENUM ('TODO', 'IN_PROGRESS', 'DONE') NOT NULL COMMENT '할 일 상태',
    color            varchar(255)                         NOT NULL COMMENT '할 일 색상',
    monthly_task_id  BIGINT                               NOT NULL COMMENT '월간 일정',
    created_at       TIMESTAMP                            NOT NULL COMMENT '생성일',
    created_by       BIGINT                               NULL COMMENT '생성한 사람',
    last_modified_at TIMESTAMP                            NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                               NULL COMMENT '최종 수정한 사람',
    deleted          BIT                                  NOT NULL COMMENT '삭제 유무'
) engine 'InnoDB' COMMENT '할 일';

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    id                BIGINT PRIMARY KEY       NOT NULL COMMENT '사용자 ID',
    email             VARCHAR(50)              NOT NULL COMMENT '이메일',
    nickname          VARCHAR(20)              NOT NULL COMMENT '닉네임',
    profile_image_url VARCHAR(2000)            NULL COMMENT '사용자 이미지',
    user_role         ENUM ('NORMAL', 'ADMIN') NOT NULL COMMENT '사용자 권한',
    created_at        TIMESTAMP                NOT NULL COMMENT '생성일',
    created_by        BIGINT                   NULL COMMENT '생성한 사람',
    last_modified_at  TIMESTAMP                NULL COMMENT '최종 수정일',
    last_modified_by  BIGINT                   NULL COMMENT '최종 수정한 사람',
    deleted           BIT                      NOT NULL COMMENT '삭제 유무',
    deleted_at        TIMESTAMP                NULL COMMENT '삭제일'
) engine 'InnoDB' COMMENT '사용자';

DROP TABLE IF EXISTS events;
CREATE TABLE IF NOT EXISTS events
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '이벤트 ID',
    name             VARCHAR(100)                      NOT NULL COMMENT '이벤트 명',
    description      VARCHAR(2500)                     NOT NULL COMMENT '설명',
    start_date       TIMESTAMP                         NOT NULL COMMENT '이벤트 시작일',
    end_date         TIMESTAMP                         NOT NULL COMMENT '이벤트 종료일',
    coupon_quantity  INT                               NOT NULL COMMENT '쿠폰 수량',
    participants     BIGINT                            NOT NULL COMMENT '참여자 수',
    expired_at       TIMESTAMP                         NULL COMMENT '만료 날짜',
    created_by       BIGINT                            NOT NULL COMMENT '생성한 사람',
    created_date     TIMESTAMP                         NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at TIMESTAMP                         NULL COMMENT '최종 수정 일시',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '이벤트';

DROP TABLE IF EXISTS event_images;
CREATE TABLE IF NOT EXISTS event_images
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '이미지 ID',
    url              VARCHAR(1500)                     NOT NULL COMMENT 'URL',
    size             BIGINT                            NOT NULL COMMENT '파일 크기',
    extension        VARCHAR(15)                       NOT NULL COMMENT '파일 확장자',
    event_id         BIGINT                            NOT NULL COMMENT '이벤트 ID',
    created_by       BIGINT                            NOT NULL COMMENT '생성한 사람',
    created_date     TIMESTAMP                         NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at TIMESTAMP                         NULL COMMENT '최종 수정 일시',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '이벤트 이미지';

CREATE TABLE IF NOT EXISTS free_coupons
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '쿠폰 ID',
    description      VARCHAR(255)                      NULL COMMENT '설명',
    serial_number    VARCHAR(255)                      NOT NULL UNIQUE COMMENT '시리얼 번호',
    issuer_id        BIGINT                            NOT NULL COMMENT '발행자 ID',
    user_id          BIGINT                            NOT NULL COMMENT '사용자 ID',
    event_id         BIGINT                            NOT NULL COMMENT '이벤트 ID',
    coupon_issuance  BOOLEAN                           NOT NULL COMMENT '쿠폰 발급 유무',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    created_date     TIMESTAMP                         NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at TIMESTAMP                         NULL COMMENT '최종 수정 일시',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '무료 쿠폰';

DROP TABLE IF EXISTS monthly_goals;
CREATE TABLE IF NOT EXISTS monthly_goals
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '월간 목표 ID',
    title            VARCHAR(50)                       NOT NULL COMMENT '제목',
    content          VARCHAR(1500)                     NOT NULL COMMENT '내용',
    done             BIT                               NOT NULL COMMENT '달성 여부',
    user_id          BIGINT                            NOT NULL COMMENT '사용자 ID',
    created_at       TIMESTAMP                         NOT NULL COMMENT '생성일',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    last_modified_at TIMESTAMP                         NOT NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    deleted          BIT                               NOT NULL COMMENT '삭제 유무'
) engine = 'InnoDB' COMMENT '월간 목표';
