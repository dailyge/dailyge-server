package project.dailyge.document.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@DisplayName("[UnitTest] MonthlyTaskDocuments 단위 테스트")
class MonthlyTaskDocumentsUnitTest {

    @Test
    @DisplayName("MonthlyTaskDocuments 객체를 생성할 수 있다.")
    void whenCreateMonthlyTaskDocumentsThenObjectShouldBeNotNull() {
        final List<MonthlyTaskDocument> tasks = MonthlyTaskDocuments.createMonthlyDocuments(1L, LocalDate.now());
        final MonthlyTaskDocuments monthlyTaskDocuments = new MonthlyTaskDocuments(tasks);

        assertNotNull(monthlyTaskDocuments);
        assertEquals(12, monthlyTaskDocuments.getTasks().size());
    }

    @Test
    @DisplayName("주어진 사용자 ID와 날짜로 12개의 MonthlyTaskDocument 객체를 생성할 수 있다.")
    void whenCreateMonthlyDocumentsWithUserIdAndDateThenReturnListOfMonthlyTaskDocuments() {
        final Long userId = 1L;
        final LocalDate date = LocalDate.now();
        final List<MonthlyTaskDocument> monthlyTaskDocuments = MonthlyTaskDocuments.createMonthlyDocuments(userId, date);

        assertNotNull(monthlyTaskDocuments);
        assertEquals(12, monthlyTaskDocuments.size());

        for (final MonthlyTaskDocument document : monthlyTaskDocuments) {
            assertNotNull(document);
            assertEquals(userId, document.getUserId());
            assertEquals(date.getYear(), document.getYear());
            assertTrue(document.getMonth() >= 1 && document.getMonth() <= 12);
        }
    }
}
