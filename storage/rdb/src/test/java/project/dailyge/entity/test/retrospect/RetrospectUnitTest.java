package project.dailyge.entity.test.retrospect;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] Retrospect 단위 테스트")
class RetrospectUnitTest {

    private static final Long ID = 1L;
    private static final String TITLE = "회고";
    private static final String CONTENT = "회고 내용";
    private static final Long USER_ID = 1L;
    private LocalDateTime now;
    private RetrospectJpaEntity retrospect;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        retrospect = new RetrospectJpaEntity(TITLE, CONTENT, now, false, USER_ID);
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 회고가 생성된다.")
    void whenValidArgumentsProvidedThenRetrospectIsCreated() {
        assertAll(
            () -> assertEquals(TITLE, retrospect.getTitle()),
            () -> assertEquals(CONTENT, retrospect.getContent()),
            () -> assertEquals(now, retrospect.getDate()),
            () -> assertFalse(retrospect.isPublic()),
            () -> assertEquals(USER_ID, retrospect.getUserId())
        );
    }

    @Test
    @DisplayName("ID를 포함한 올바른 인자가 들어오면 회고가 생성된다.")
    void whenValidArgumentsProvidedIncludingIdThenRetrospectIsCreated() {
        final LocalDateTime now = LocalDateTime.now();
        final RetrospectJpaEntity newRetrospect = new RetrospectJpaEntity(ID, TITLE, CONTENT, now, false, USER_ID);

        assertAll(
            () -> assertEquals(ID, newRetrospect.getId()),
            () -> assertEquals(TITLE, newRetrospect.getTitle()),
            () -> assertEquals(CONTENT, newRetrospect.getContent()),
            () -> assertEquals(now, newRetrospect.getDate()),
            () -> assertFalse(newRetrospect.isPublic()),
            () -> assertEquals(USER_ID, newRetrospect.getUserId())
        );
    }

    @Test
    @DisplayName("회고를 수정하면 내용이 반영 된다.")
    void whenUpdateThenFieldsShouldBeApplied() {
        final String newTitle = "회고 제목 수정";
        final String newContent = "회고 제목 수정";
        final LocalDateTime newDate = LocalDateTime.now();
        retrospect.update(newTitle, newContent, newDate, true);

        assertAll(
            () -> assertEquals(newTitle, retrospect.getTitle()),
            () -> assertEquals(newContent, retrospect.getContent()),
            () -> assertEquals(newDate, retrospect.getDate()),
            () -> assertTrue(retrospect.isPublic())
        );
    }

    @Test
    @DisplayName("회고를 삭제하면 deleted 필드가 True가 된다.")
    void whenDeletedThenDeleteStatusShouldBeTrue() {
        retrospect.delete();
        assertTrue(retrospect.getDeleted());
    }
}
