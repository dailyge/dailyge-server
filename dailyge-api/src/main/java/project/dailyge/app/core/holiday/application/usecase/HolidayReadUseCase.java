package project.dailyge.app.core.holiday.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.holiday.application.HolidayReadService;
import project.dailyge.entity.holiday.HolidayEntityReadRepository;
import project.dailyge.entity.holiday.HolidayJpaEntity;

import java.time.LocalDate;
import java.util.List;

@ApplicationLayer(value = "HolidayReadUseCase")
class HolidayReadUseCase implements HolidayReadService {

    private final HolidayEntityReadRepository holidayEntityReadRepository;

    public HolidayReadUseCase(final HolidayEntityReadRepository holidayEntityReadRepository) {
        this.holidayEntityReadRepository = holidayEntityReadRepository;
    }

    @Override
    public List<HolidayJpaEntity> findHolidaysByDate(
        final Long countryId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        return holidayEntityReadRepository.findHolidaysByDate(countryId, startDate, endDate);
    }
}
