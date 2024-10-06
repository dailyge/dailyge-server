package project.dailyge.app.test.common;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.core.holiday.presentation.validator.HolidayClientValidator;

@DisplayName("[UnitTest] HolidayClientValidator 단위 테스트")
class HolidayClientValidatorTest {

    private HolidayClientValidator validator;

    @BeforeEach
    void setUp() {
        validator = new HolidayClientValidator();
    }

    @ParameterizedTest
    @ValueSource(ints = {2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2099})
    @DisplayName("유효한 년도를 입력하면 예외가 발생하지 않는다.")
    void whenValidYearThenNoExceptionThrown(int year) {
        assertDoesNotThrow(() -> validator.validateYear(year));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020})
    @DisplayName("2021년 이전의 공휴일을 조회하면 IllegalArgumentException이 발생한다.")
    void whenYearIsZeroOrNegativeThenThrowException(int year) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateYear(year));
    }

    @ParameterizedTest
    @ValueSource(ints = {2100, 2200, 3000})
    @DisplayName("년도 값이 2100 이상일 때, IllegalArgumentException 예외가 발생한다.")
    void whenYearExceeds2100ThenThrowException(int year) {
        assertThrows(IllegalArgumentException.class, () -> validator.validateYear(year));
    }
}
