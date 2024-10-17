package project.dailyge.app.test.weeklygoal.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import project.dailyge.app.core.weeklygoal.presentation.validator.WeeklyGoalClientValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[UnitTest] WeeklyGoalClientValidator 단위 테스트")
class WeeklyGoalClientValidatorUnitTest {

    private WeeklyGoalClientValidator weeklyGoalClientValidator;

    @BeforeEach
    void setUp() {
        weeklyGoalClientValidator = new WeeklyGoalClientValidator();
    }

    @ParameterizedTest
    @CsvSource({
        "2024-10-14",
        "2024-10-07",
        "2024-09-30"
    })
    @DisplayName("주간 시작 날짜가 월요일이면 예외를 던지지 않는다.")
    void whenDayOfWeekIsMondayThenShouldNotThrowException(String dateTimeString) {
        final LocalDate weekStartDate = LocalDate.parse(dateTimeString);
        assertDoesNotThrow(() -> weeklyGoalClientValidator.validateWeekStartDate(weekStartDate));
    }

    @ParameterizedTest
    @CsvSource({
        "2024-10-13",
        "2024-10-16",
        "2024-10-17"
    })
    @DisplayName("주간 시작 날짜가 월요일이 아니면 예외를 던진다.")
    void whenDayOfWeekIsNotMondayThenShouldThrowIllegalArgumentException(String dateTimeString) {
        final LocalDate weekStartDate = LocalDate.parse(dateTimeString);
        assertThrows(IllegalArgumentException.class, () -> weeklyGoalClientValidator.validateWeekStartDate(weekStartDate));
    }
}
