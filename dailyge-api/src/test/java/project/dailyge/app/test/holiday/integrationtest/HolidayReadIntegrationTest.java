package project.dailyge.app.test.holiday.integrationtest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.holiday.application.HolidayReadService;
import project.dailyge.app.core.holiday.application.HolidayWriteService;
import project.dailyge.entity.holiday.HolidayJpaEntity;

import java.time.LocalDate;
import java.util.List;

@DisplayName("[IntegrationTest] Holiday 조회 통합 테스트")
class HolidayReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private HolidayWriteService holidayWriteService;

    @Autowired
    private HolidayReadService holidayReadService;

    @Test
    @DisplayName("공휴일이 존재하면 공휴일 목록을 조회할 수 있다.")
    void whenHolidaysExistsThenResultShouldNotBeEmpty() {
        final LocalDate newYearStartDate = LocalDate.of(2024, 1, 1);
        final LocalDate newYearEndDate = LocalDate.of(2024, 12, 31);

        final HolidayJpaEntity newHoliday = new HolidayJpaEntity(null, "새해", newYearStartDate, true, 1L);
        holidayWriteService.save(newHoliday);

        final List<HolidayJpaEntity> findHolidays = holidayReadService.findHolidaysByDate(newHoliday.getCountryId(), newYearStartDate, newYearEndDate);
        assertFalse(findHolidays.isEmpty());
    }

    @Test
    @DisplayName("공휴일이 존재하지 않으면 빈 리스트를 반환한다.")
    void whenHolidaysNotExistsThenResultShouldNotBeEmpty() {
        final LocalDate newYearStartDate = LocalDate.of(2024, 1, 1);
        final LocalDate newYearEndDate = LocalDate.of(2024, 12, 31);

        final HolidayJpaEntity newHoliday = new HolidayJpaEntity(null, "새해", newYearStartDate, true, 1L);
        holidayWriteService.save(newHoliday);

        final List<HolidayJpaEntity> findHolidays = holidayReadService.findHolidaysByDate(
            newHoliday.getCountryId(), newYearStartDate.plusYears(1), newYearEndDate.plusYears(1)
        );
        assertTrue(findHolidays.isEmpty());
    }
}
