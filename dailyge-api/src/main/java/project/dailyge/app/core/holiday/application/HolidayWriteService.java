package project.dailyge.app.core.holiday.application;

import project.dailyge.entity.holiday.HolidayJpaEntity;

public interface HolidayWriteService {
    Long save(HolidayJpaEntity holiday);
}
