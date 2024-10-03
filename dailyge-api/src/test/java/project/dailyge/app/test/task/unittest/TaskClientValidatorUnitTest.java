package project.dailyge.app.test.task.unittest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.dailyge.app.core.task.presentation.validator.TaskClientValidator;

import java.time.LocalDate;
import java.util.stream.IntStream;

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
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("endDate가 startDate보다 이후일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsAfterOrEqualStartDate_thenNoException() {
        final LocalDate startDate = LocalDate.of(2023, 8, 24);
        final LocalDate endDate = LocalDate.of(2023, 8, 25);
        assertDoesNotThrow(() -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("provideGreaterThan8Days")
    @DisplayName("endDate와 startDate의 차이가 43일을 초과할 경우 IllegalArgumentException이 발생한다.")
    void whenEndDateExceedsMaxDaysDifference_thenThrowIllegalArgumentException(final int parameter) {
        final LocalDate startDate = LocalDate.of(2023, 8, 1);
        final LocalDate endDate = LocalDate.of(2023, 9, parameter);
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("provideAugustDays")
    @DisplayName("endDate와 startDate의 차이가 43일 이하일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsExactlyMaxDaysDifference_thenNoException(int parameter) {
        final LocalDate startDate = LocalDate.of(2023, 8, parameter);
        final LocalDate endDate = LocalDate.of(2023, 9, 6);
        assertDoesNotThrow(() -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("월간 통계 검증 시 endDate가 startDate보다 이전일 경우 예외가 발생 한다")
    void whenEndDateIsBeforeStartDateByMonthlyStatistic_thenThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.minusMonths(3);
        assertThrows(IllegalArgumentException.class, () -> validator.validateOneMonthDifference(startDate, endDate));
    }

    @Test
    @DisplayName("월간 통계 검증 시 한달의 차이가 나는지 확인한다.")
    void whenDateDifferenceOneMonth_thenNoException() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusMonths(1);
        assertDoesNotThrow(() -> validator.validateOneMonthDifference(startDate, endDate));
    }

    @Test
    @DisplayName("월간 통계 검증 시 한달의 차이가 아니면 예외가 발생한다.")
    void whenDateNotDifferenceOneMonth_thenThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate twoMonthPlusEndDate = startDate.plusMonths(2);
        final LocalDate sameMonthEndDate = startDate.plusMonths(2);

        assertThrows(IllegalArgumentException.class, () -> validator.validateOneMonthDifference(startDate, twoMonthPlusEndDate));
        assertThrows(IllegalArgumentException.class, () -> validator.validateOneMonthDifference(startDate, sameMonthEndDate));
    }

    private static IntStream provideAugustDays() {
        return IntStream.rangeClosed(1, 31);
    }

    private static IntStream provideGreaterThan8Days() {
        return IntStream.rangeClosed(14, 30);
    }
}
