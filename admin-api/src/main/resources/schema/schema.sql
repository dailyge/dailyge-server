CREATE TABLE events
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '이벤트 ID',
    name             VARCHAR(100)                      NOT NULL COMMENT '이벤트 명',
    description      VARCHAR(2500)                     NOT NULL COMMENT '설명',
    start_date       DATETIME(6)                       NOT NULL COMMENT '이벤트 시작일',
    end_date         DATETIME(6)                       NOT NULL COMMENT '이벤트 종료일',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    deleted          BIT                               NOT NULL COMMENT '삭제 유무'
) engine 'InnoDB' COMMENT '이벤트';

CREATE TABLE free_coupons
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '쿠폰 ID',
    description      VARCHAR(100)                      NULL COMMENT '설명',
    serial_number    VARCHAR(255)                      NOT NULL UNIQUE COMMENT '쿠폰 번호',
    issuer_id        BIGINT                            NOT NULL COMMENT '발급자 ID',
    user_id          BIGINT                            NULL COMMENT '당첨자 ID',
    event_id         BIGINT                            NOT NULL COMMENT '이벤트 ID',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    deleted          BIT                               NOT NULL COMMENT '삭제 유무'
) engine 'InnoDB' COMMENT '무료 쿠폰';
