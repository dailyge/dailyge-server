package project.dailyge.app.test.monthlygoal.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.monthlygoal.application.validator.MonthlyGoalValidator;

@DisplayName("[UnitTest] MonthlyGoalValidator 클래스 단위 테스트")
class MonthlyGoalValidatorUnitTest {

    private MonthlyGoalValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MonthlyGoalValidator();
    }

    @Test
    @DisplayName("year가 null일 경우 IllegalArgumentException이 발생한다.")
    void whenYearIsNullThenThrowException() {
        final Integer year = null;
        final Integer month = 9;

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateYearAndMonth(year, month), "올바른 년/월을 입력해주세요."
        );
    }

    @Test
    @DisplayName("month가 null일 경우 IllegalArgumentException이 발생한다.")
    void whenMonthIsNullThenThrowException() {
        final Integer year = 2024;
        final Integer month = null;

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateYearAndMonth(year, month), "올바른 년/월을 입력해주세요."
        );
    }

    @Test
    @DisplayName("month가 0 이하일 경우 IllegalArgumentException이 발생한다.")
    void whenMonthIsLessThanOrEqualToZeroThenThrowException() {
        final Integer year = 2024;
        final Integer month = 0;

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateYearAndMonth(year, month), "올바른 년/월을 입력해주세요."
        );
    }

    @Test
    @DisplayName("month가 13 이상일 경우 IllegalArgumentException이 발생한다.")
    void whenMonthIsGreaterThanOrEqualToThirteenThenThrowException() {
        final Integer year = 2024;
        final Integer month = 13;

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateYearAndMonth(year, month), "올바른 년/월을 입력해주세요."
        );
    }

    @Test
    @DisplayName("year와 month가 유효한 경우 예외가 발생하지 않는다.")
    void whenYearAndMonthAreValidThenNoException() {
        final Integer year = 2024;
        final Integer month = 9;
        validator.validateYearAndMonth(year, month);
    }
}
