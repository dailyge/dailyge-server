CREATE TABLE IF NOT EXISTS events
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '이벤트 ID',
    name             VARCHAR(100)                      NOT NULL COMMENT '이벤트 명',
    description      VARCHAR(2500)                     NOT NULL COMMENT '설명',
    start_date       DATETIME(6)                       NOT NULL COMMENT '이벤트 시작일',
    end_date         DATETIME(6)                       NOT NULL COMMENT '이벤트 종료일',
    coupon_quantity  INT                               NOT NULL COMMENT '쿠폰 수량',
    participants     BIGINT                            NOT NULL COMMENT '참여자 수',
    expired_at       DATETIME                          NULL COMMENT '만료 날짜',
    created_by       BIGINT                            NOT NULL COMMENT '생성한 사람',
    created_date     DATETIME(6)                       NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정 일시',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '이벤트';

CREATE TABLE IF NOT EXISTS event_images
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '이미지 ID',
    url              VARCHAR(1500)                     NOT NULL COMMENT 'URL',
    size             BIGINT                            NOT NULL COMMENT '파일 크기',
    extension        VARCHAR(15)                       NOT NULL COMMENT '파일 확장자',
    event_id         BIGINT                            NOT NULL COMMENT '이벤트 ID',
    created_by       BIGINT                            NOT NULL COMMENT '생성한 사람',
    created_date     DATETIME(6)                       NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정 일시',
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
    created_date     DATETIME(6)                       NOT NULL COMMENT '생성 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정 일시',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '무료 쿠폰';
