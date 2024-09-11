package project.dailyge.entity.notice;

import static project.dailyge.entity.notice.NoticeType.UPDATE;

public final class NoticeFixture {

    private NoticeFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final NoticeJpaEntity newNotice = new NoticeJpaEntity(
        1L,
        "공지 사항",
        "공지 사항 내용",
        UPDATE,
        1L
    );
}
