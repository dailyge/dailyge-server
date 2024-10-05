package project.dailyge.app.test.anniversary.unittest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.dailyge.app.core.anniversary.presentation.validator.AnniversaryClientValidator;

import java.time.LocalDate;
import java.util.stream.IntStream;

@DisplayName("[UnitTest] AnniversaryClientValidator 단위 테스트")
class AnniversaryClientValidatorUnitTest {

    private AnniversaryClientValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AnniversaryClientValidator();
    }

    @Test
    @DisplayName("endDate가 startDate보다 이전일 경우 예외가 발생한다")
    void whenEndDateIsBeforeStartDateThenThrowIllegalArgumentException() {
        final LocalDate startDate = LocalDate.of(2023, 8, 25);
        final LocalDate endDate = LocalDate.of(2023, 8, 24);
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("endDate가 startDate보다 이후일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsAfterOrEqualStartDateThenNoException() {
        final LocalDate startDate = LocalDate.of(2023, 8, 24);
        final LocalDate endDate = LocalDate.of(2023, 8, 25);
        assertDoesNotThrow(() -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("startDate와 endDate가 null일 경우 예외가 발생한다.")
    void whenStartDateOrEndDateIsNullThenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(LocalDate.now(), null));
    }

    @ParameterizedTest
    @MethodSource("provideDaysExceedingMaxDifference")
    @DisplayName("endDate와 startDate의 차이가 43일을 초과할 경우 예외가 발생한다.")
    void whenEndDateExceedsMaxDaysDifferenceThenThrowIllegalArgumentException(final int daysToAdd) {
        final LocalDate startDate = LocalDate.of(2023, 8, 1);
        final LocalDate endDate = startDate.plusDays(daysToAdd);
        assertThrows(IllegalArgumentException.class, () -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("provideDaysWithinMaxDifference")
    @DisplayName("endDate와 startDate의 차이가 43일 이하일 경우 예외가 발생하지 않는다.")
    void whenEndDateIsWithinMaxDaysDifferenceThenNoException(final int daysToAdd) {
        final LocalDate startDate = LocalDate.of(2023, 8, 1);
        final LocalDate endDate = startDate.plusDays(daysToAdd);
        assertDoesNotThrow(() -> validator.validateFromStartDateToEndDate(startDate, endDate));
    }

    private static IntStream provideDaysExceedingMaxDifference() {
        return IntStream.rangeClosed(44, 60);
    }

    private static IntStream provideDaysWithinMaxDifference() {
        return IntStream.rangeClosed(0, 43);
    }
}
