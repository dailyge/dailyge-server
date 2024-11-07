package project.dailyge.app.test.task.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.dailyge.app.core.task.presentation.validator.TaskRecurrenceClientValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static project.dailyge.entity.task.RecurrenceType.MONTHLY;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;

@DisplayName("[UnitTest] TaskRecurrenceClientValidator 단위 테스트")
class TaskRecurrenceClientValidatorUnitTest {

    private TaskRecurrenceClientValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskRecurrenceClientValidator();
    }

    @Test
    @DisplayName("endDate가 startDate보다 이전일 경우 예외가 발생 한다")
    void whenEndDateIsBeforeStartDateThenShouldThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.of(2024, 8, 25);
        final LocalDate endDate = LocalDate.of(2024, 8, 24);
        assertThrows(IllegalArgumentException.class, () -> validator.validateStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("endDate가 startDate 간 기간이 일년 이상 차이나면 예외가 발생한다.")
    void whenDurationExceedsOneYearThenShouldThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.of(2024, 8, 24);
        final LocalDate endDate = LocalDate.of(2025, 9, 25);
        assertThrows(IllegalArgumentException.class, () -> validator.validateStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("endDate가 startDate보다 이후일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsAfterOrEqualStartDateThenNoException() {
        final LocalDate startDate = LocalDate.of(2024, 8, 24);
        final LocalDate endDate = LocalDate.of(2025, 8, 24);
        assertDoesNotThrow(() -> validator.validateStartDateToEndDate(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("provideWeeklyDayPatterns")
    @DisplayName("주간 날짜 패턴이 유효하면 예외를 던지지 않는다.")
    void whenValidWeeklyDatePatternThenShouldNotThrowException(final List<Integer> days) {
        assertDoesNotThrow(() -> validator.validateDayPattern(WEEKLY, days));
    }

    private static Stream<List<Integer>> provideWeeklyDayPatterns() {
        return Stream.of(
            List.of(1, 2, 3, 4, 5),
            List.of(6),
            List.of(1, 3, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidWeeklyDayPatterns")
    @DisplayName("주간 날짜 패턴이 유효하지 않으면 예외를 던진다.")
    void whenInvalidWeeklyDatePatternThenShouldThrowException(final List<Integer> days) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateDayPattern(WEEKLY, days));
    }

    private static Stream<List<Integer>> provideInvalidWeeklyDayPatterns() {
        return Stream.of(
            List.of(5, 4, 3, 2, 1),
            List.of(0),
            List.of(2, 8)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMonthlyDayPatterns")
    @DisplayName("월간 날짜 패턴이 유효하면 예외를 던지지 않는다.")
    void whenValidMonthlyDatePatternThenShouldNotThrowException(final List<Integer> days) {
        assertDoesNotThrow(() -> validator.validateDayPattern(MONTHLY, days));
    }

    private static Stream<List<Integer>> provideMonthlyDayPatterns() {
        return Stream.of(
            List.of(1),
            List.of(25),
            List.of(31)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMonthlyDayPatterns")
    @DisplayName("월간 날짜 패턴이 유효하지 않으면 예외를 던진다.")
    void whenInvalidMonthlyDatePatternThenShouldThrowException(final List<Integer> days) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateDayPattern(MONTHLY, days));
    }

    private static Stream<List<Integer>> provideInvalidMonthlyDayPatterns() {
        return Stream.of(
            List.of(1, 2, 3),
            List.of(32),
            List.of(0)
        );
    }
}
