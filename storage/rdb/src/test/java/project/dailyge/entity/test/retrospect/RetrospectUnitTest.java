package project.dailyge.entity.test.retrospect;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("[UnitTest] Retrospect 단위 테스트")
class RetrospectUnitTest {

    private static final Long ID = 1L;
    private static final String TITLE = "회고";
    private static final String CONTENT = "회고 내용";
    private static final Long USER_ID = 1L;

    @Test
    @DisplayName("올바른 인자가 들어오면 회고가 생성된다.")
    void whenValidArgumentsProvidedThenRetrospectIsCreated() {
        final LocalDateTime now = LocalDateTime.now();
        final RetrospectJpaEntity newRetrospect = new RetrospectJpaEntity(TITLE, CONTENT, now, false, USER_ID);

        assertAll(
            () -> assertEquals(TITLE, newRetrospect.getTitle()),
            () -> assertEquals(CONTENT, newRetrospect.getContent()),
            () -> assertEquals(now, newRetrospect.getDate()),
            () -> assertFalse(newRetrospect.isPublic()),
            () -> assertEquals(USER_ID, newRetrospect.getUserId())
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
    @DisplayName("기본 생성자를 통해 회고를 생성할 수 있다.")
    void whenDefaultConstructorIsUsedThenRetrospectHasDefaultValues() {
        final RetrospectJpaEntity newRetrospect = new RetrospectJpaEntity();

        assertAll(
            () -> assertNull(newRetrospect.getId()),
            () -> assertNull(newRetrospect.getTitle()),
            () -> assertNull(newRetrospect.getContent()),
            () -> assertNull(newRetrospect.getDate()),
            () -> assertFalse(newRetrospect.isPublic()),
            () -> assertNull(newRetrospect.getUserId())
        );
    }
}
