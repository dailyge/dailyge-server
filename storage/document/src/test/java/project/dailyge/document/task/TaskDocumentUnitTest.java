package project.dailyge.document.task;

import com.fasterxml.uuid.Generators;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("[UnitTest]")
class TaskDocumentUnitTest {

    @Test
    @DisplayName("Task 문서를 생성할 수 있다.")
    void whenCreateTaskDocumentResultShouldBeNotNull() {
        final TaskDocument document = new TaskDocument(
            Generators.timeBasedGenerator().generate().toString(),
            "Backend 팀과 미팅",
            "오전 Backend 팀과 미팅",
            LocalDate.now(),
            "DONE",
            1L
        );

        assertNotNull(document);
    }

    @Test
    @DisplayName("LocalDate는 내부 필드값을 통ㅎ애 조합할 수 있다.")
    void whenGetLocalDateThenResultShouldBeSameOfYearAndMonthAndDay() {
        final TaskDocument document = new TaskDocument(
            Generators.timeBasedGenerator().generate().toString(),
            "Backend 팀과 미팅",
            "오전 Backend 팀과 미팅",
            LocalDate.now(),
            "DONE",
            1L
        );

        assertEquals(LocalDate.of(document.getYear(), document.getMonth(), document.getDay()), document.getLocalDate());
    }
}
