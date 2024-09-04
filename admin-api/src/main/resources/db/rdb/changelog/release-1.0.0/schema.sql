CREATE TABLE IF NOT EXISTS code_and_message
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT 'ID',
    domain           VARCHAR(255)                      NOT NULL COMMENT '도메인',
    name             VARCHAR(255)                      NOT NULL COMMENT '이름',
    code             INT                               NOT NULL COMMENT '코드',
    message          VARCHAR(255)                      NOT NULL COMMENT '메시지',
    created_at       DATETIME(6)                       NOT NULL COMMENT '생성 일시',
    created_by       BIGINT                            NULL COMMENT '생성자',
    last_modified_at DATETIME(6)                       NULL COMMENT '최종 수정 일시',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정자',
    deleted          BIT                               NOT NULL DEFAULT 0 COMMENT '삭제 여부'
) ENGINE = InnoDB COMMENT '코드 & 메시지';
