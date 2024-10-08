package project.dailyge.app.core.anniversary.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDate;
import java.util.List;

public interface AnniversaryReadService {
    AnniversaryJpaEntity findById(Long anniversaryId);

    List<AnniversaryJpaEntity> findByDates(DailygeUser dailygeUser, LocalDate startDate, LocalDate endDate);
}
