package project.dailyge.document.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static project.dailyge.document.task.MonthlyTaskDocument.createMonthlyDocument;

@DisplayName("[UnitTest] MonthlyTaskDocument 단위 테스트")
class MonthlyTaskDocumentUnitTest {

    @Test
    @DisplayName("createMonthlyDocument 메서드를 통해 MonthlyTaskDocument 객체를 생성할 수 있다.")
    void whenCreateMonthlyDocumentThenObjectIsCorrect() {
        final Long userId = 123L;
        final int year = 2023;
        final int month = 7;

        final MonthlyTaskDocument document = createMonthlyDocument(userId, year, month);

        assertAll(
            () -> assertNotNull(document),
            () -> assertNotNull(document.getId()),
            () -> assertEquals(userId, document.getUserId()),
            () -> assertEquals(year, document.getYear()),
            () -> assertEquals(month, document.getMonth())
        );
    }

    @Test
    @DisplayName("userId가 소유자인지 확인할 수 있다.")
    void whenIsOwnerThenReturnCorrect() {
        final Long userId = 123L;
        final int year = 2023;
        final int month = 7;
        final MonthlyTaskDocument document = createMonthlyDocument(userId, year, month);

        assertAll(
            () -> assertTrue(document.isOwner(userId)),
            () -> assertFalse(document.isOwner(456L))
        );
    }
}

