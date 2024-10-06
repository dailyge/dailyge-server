package project.dailyge.app.core.holiday.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.holiday.application.HolidayWriteService;
import project.dailyge.entity.holiday.HolidayEntityWriteRepository;
import project.dailyge.entity.holiday.HolidayJpaEntity;

@RequiredArgsConstructor
@ApplicationLayer(value = "HolidayWriteUseCase")
class HolidayWriteUseCase implements HolidayWriteService {

    private final HolidayEntityWriteRepository holidayEntityWriteRepository;

    @Override
    @Transactional
    public Long save(HolidayJpaEntity holiday) {
        return holidayEntityWriteRepository.save(holiday);
    }
}
