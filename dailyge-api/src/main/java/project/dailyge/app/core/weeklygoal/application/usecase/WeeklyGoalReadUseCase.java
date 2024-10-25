package project.dailyge.app.core.weeklygoal.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalReadService;
import project.dailyge.app.core.weeklygoal.persistence.WeeklyGoalReadDao;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "WeeklyGoalReadUseCase")
class WeeklyGoalReadUseCase implements WeeklyGoalReadService {

    private final WeeklyGoalReadDao weeklyGoalReadDao;

    @Override
    public List<WeeklyGoalJpaEntity> findPageByCursor(
        final DailygeUser dailygeUser,
        final Cursor cursor,
        final LocalDateTime weekStartDate
    ) {
        return weeklyGoalReadDao.findByCursor(dailygeUser.getUserId(), cursor, weekStartDate);
    }
}
