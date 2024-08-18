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

DROP TABLE IF EXISTS notice;
CREATE TABLE IF NOT EXISTS notice
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '공지 사항 ID'
    title            VARCHAR(50)                       NOT NULL COMMENT '제목',
    content          VARCHAR(3000)                     NOT NULL COMMENT '내용',
    notice_type      ENUM ('EVENT', 'UPDATE', 'BUG')   NOT NULL COMMENT '공지 사항 유형',
    user_id          BIGINT                            NOT NULL COMMENT '사용자 ID',
    created_at       DATETIME(6)                       NOT NULL COMMENT '생성일',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    last_modified_at DATETIME(6)                       NOT NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    deleted          BIT                               NOT NULL COMMENT '삭제 유무'
    ) engine = 'InnoDB' COMMENT '공지 사항';
