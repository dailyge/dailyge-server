package project.dailyge.app.core.holiday.application;

import project.dailyge.entity.holiday.HolidayJpaEntity;

import java.time.LocalDate;
import java.util.List;

public interface HolidayReadService {
    List<HolidayJpaEntity> findHolidaysByDate(Long countryId, LocalDate startDate, LocalDate endDate);
}
