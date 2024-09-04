package project.dailyge.app.test.task.unittest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.task.presentation.validator.TaskClientValidator;

import java.time.LocalDate;

@DisplayName("[UnitTest] TaskClientValidator 단위 테스트")
class TaskClientValidatorUnitTest {

    private TaskClientValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskClientValidator();
    }

    @Test
    @DisplayName("endDate가 startDate보다 이전일 경우 예외가 발생 한다")
    void whenEndDateIsBeforeStartDate_thenThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.of(2023, 8, 25);
        final LocalDate endDate = LocalDate.of(2023, 8, 24);
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validateFromStartDateToEndDate(startDate, endDate);
        });
    }

    @Test
    @DisplayName("endDate가 startDate보다 이후일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsAfterOrEqualStartDate_thenNoException() {
        final LocalDate startDate = LocalDate.of(2023, 8, 24);
        final LocalDate endDate = LocalDate.of(2023, 8, 25);
        assertDoesNotThrow(() -> {
            validator.validateFromStartDateToEndDate(startDate, endDate);
        });
    }
}
