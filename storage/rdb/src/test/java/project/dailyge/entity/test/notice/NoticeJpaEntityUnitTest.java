package project.dailyge.entity.test.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.notice.NoticeJpaEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.entity.notice.NoticeFixture.newNotice;
import static project.dailyge.entity.notice.NoticeType.UPDATE;

@DisplayName("[UnitTest] NoticeJpaEntity 단위 테스트")
class NoticeJpaEntityUnitTest {

    private NoticeJpaEntity notice;

    @BeforeEach
    void setUp() {
        notice = newNotice;
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 공지 사항이 생성된다.")
    void whenValidArgumentsProvidedThenNoticeIsCreated() {
        final NoticeJpaEntity newNotice = notice;

        assertAll(
            () -> assertEquals(1L, newNotice.getId()),
            () -> assertEquals("공지 사항", newNotice.getTitle()),
            () -> assertEquals("공지 사항 내용", newNotice.getContent()),
            () -> assertEquals(UPDATE, newNotice.getNoticeType()),
            () -> assertEquals(1L, newNotice.getUserId())
        );
    }
}
