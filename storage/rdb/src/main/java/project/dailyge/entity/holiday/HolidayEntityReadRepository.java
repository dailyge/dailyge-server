package project.dailyge.entity.holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayEntityReadRepository {
    List<HolidayJpaEntity> findHolidaysByDate(Long countryId, LocalDate startDate, LocalDate endDate);
}
