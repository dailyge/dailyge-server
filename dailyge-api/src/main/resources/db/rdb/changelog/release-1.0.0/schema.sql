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
    created_at       DATETIME(6)                          NOT NULL COMMENT '생성일',
    created_by       BIGINT                               NULL COMMENT '생성한 사람',
    last_modified_at DATETIME(6)                          NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                               NULL COMMENT '최종 수정한 사람',
    deleted          BIT                                  NOT NULL COMMENT '삭제 유무'
) engine 'InnoDB' COMMENT '할 일';

CREATE TABLE IF NOT EXISTS user_sequence
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '사용자 PK',
    email    VARCHAR(50)                       NOT NULL COMMENT '이메일',
    executed BIT                               NOT NULL DEFAULT FALSE COMMENT '처리 여부'
) ENGINE InnoDB COMMENT '사용자 채번 테이블';

CREATE TABLE IF NOT EXISTS users
(
    id                BIGINT PRIMARY KEY       NOT NULL COMMENT '사용자 ID',
    email             VARCHAR(50)              NOT NULL COMMENT '이메일',
    nickname          VARCHAR(20)              NOT NULL COMMENT '닉네임',
    profile_image_url VARCHAR(2000)            NULL COMMENT '사용자 이미지',
    user_role         ENUM ('NORMAL', 'ADMIN') NOT NULL COMMENT '사용자 권한',
    created_at        DATETIME(6)              NOT NULL COMMENT '생성일',
    created_by        BIGINT                   NULL COMMENT '생성한 사람',
    last_modified_at  DATETIME(6)              NULL COMMENT '최종 수정일',
    last_modified_by  BIGINT                   NULL COMMENT '최종 수정한 사람',
    deleted           BIT                      NOT NULL COMMENT '삭제 유무',
    deleted_at        DATETIME(6)              NULL COMMENT '삭제일'
) engine 'InnoDB' COMMENT '사용자';

DROP TABLE IF EXISTS monthly_gols;
CREATE TABLE IF NOT EXISTS monthly_gols
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '월간 목표 ID',
    title            VARCHAR(50)                       NOT NULL COMMENT '제목',
    content          VARCHAR(1500)                     NOT NULL COMMENT '내용',
    done             BIT                               NOT NULL COMMENT '달성 여부',
    user_id          BIGINT                            NOT NULL COMMENT '사용자 ID',
    created_at       DATETIME(6)                       NOT NULL COMMENT '생성일',
    created_by       BIGINT                            NULL COMMENT '생성한 사람',
    last_modified_at DATETIME(6)                       NOT NULL COMMENT '최종 수정일',
    last_modified_by BIGINT                            NULL COMMENT '최종 수정한 사람',
    deleted          BIT                               NOT NULL COMMENT '삭제 유무'
) engine = 'InnoDB' COMMENT '월간 목표';
