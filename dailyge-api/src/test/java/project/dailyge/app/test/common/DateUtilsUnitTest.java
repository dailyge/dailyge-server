package project.dailyge.app.test.common;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.common.utils.DateUtils;

import java.time.LocalDate;

@DisplayName("[UnitTest] DateUtils 단위 테스트")
class DateUtilsUnitTest {

    @ParameterizedTest
    @ValueSource(ints = {2000, 2020, 1999, 2021, 1900, 2100})
    @DisplayName("연도의 첫 시작일을 반환한다.")
    void whenGetYearStartDateThenReturnFirstDayOfYear(final int year) {
        final LocalDate expectedStartDate = LocalDate.of(year, 1, 1);
        final LocalDate result = DateUtils.getYearStartDate(year);

        assertEquals(expectedStartDate, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {2000, 2020, 1999, 2021, 1900, 2100})
    @DisplayName("연도의 마지막 날짜를 반환한다.")
    void whenGetYearEndDateThenReturnLastDayOfYear(final int year) {
        final LocalDate expectedEndDate = LocalDate.of(year, 12, 31);
        final LocalDate result = DateUtils.getYearEndDate(year);

        assertEquals(expectedEndDate, result);
    }

    @Test
    @DisplayName("윤년의 첫 번째 날과 마지막 날을 반환한다.")
    void whenLeapYearThenReturnCorrectDates() {
        final int leapYear = 2020;
        assertAll(
            () -> assertEquals(LocalDate.of(leapYear, 1, 1), DateUtils.getYearStartDate(leapYear)),
            () -> assertEquals(LocalDate.of(leapYear, 12, 31), DateUtils.getYearEndDate(leapYear))
        );
    }

    @Test
    @DisplayName("비윤년의 첫 번째 날과 마지막 날을 반환한다.")
    void whenNonLeapYearThenReturnCorrectDates() {
        final int nonLeapYear = 2019;
        assertAll(
            () -> assertEquals(LocalDate.of(nonLeapYear, 1, 1), DateUtils.getYearStartDate(nonLeapYear)),
            () -> assertEquals(LocalDate.of(nonLeapYear, 12, 31), DateUtils.getYearEndDate(nonLeapYear))
        );
    }
}
