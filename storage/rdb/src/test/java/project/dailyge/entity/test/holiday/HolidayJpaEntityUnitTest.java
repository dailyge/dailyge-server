package project.dailyge.entity.test.holiday;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.holiday.HolidayJpaEntity;

import java.time.LocalDate;

@DisplayName("[UnitTest] HolidayJpaEntity 엔티티 테스트")
class HolidayJpaEntityUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 Holiday 객체가 생성된다.")
    void whenValidParametersThenHolidayJpaEntityShouldBeCreated() {
        final LocalDate holidayDate = LocalDate.of(2024, 1, 1);
        final HolidayJpaEntity holiday = new HolidayJpaEntity(1L, "New Year's Day", holidayDate, true, 410L);

        assertAll(
            () -> assertEquals(1L, holiday.getId()),
            () -> assertEquals("New Year's Day", holiday.getName()),
            () -> assertEquals(holidayDate, holiday.getDate()),
            () -> assertTrue(holiday.isHoliday()),
            () -> assertEquals(410L, holiday.getCountryId())
        );
    }

    @Test
    @DisplayName("날짜를 String으로 변환할 수 있다.")
    void whenGetDateAsStringThenReturnCorrectString() {
        final LocalDate holidayDate = LocalDate.of(2024, 1, 1);
        final HolidayJpaEntity holiday = new HolidayJpaEntity(1L, "New Year's Day", holidayDate, true, 410L);

        assertThat(holiday.getDateAsString()).isEqualTo("2024-01-01");
    }
}
