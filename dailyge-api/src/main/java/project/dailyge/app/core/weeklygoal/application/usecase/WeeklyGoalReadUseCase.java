package project.dailyge.app.core.weeklygoal.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalReadService;
import project.dailyge.app.core.weeklygoal.persistence.WeeklyGoalReadDao;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationLayer(value = "WeeklyGoalReadUseCase")
class WeeklyGoalReadUseCase implements WeeklyGoalReadService {

    private final WeeklyGoalReadDao weeklyGoalReadDao;

    public WeeklyGoalReadUseCase(final WeeklyGoalReadDao weeklyGoalReadDao) {
        this.weeklyGoalReadDao = weeklyGoalReadDao;
    }

    @Override
    public List<WeeklyGoalJpaEntity> findPageByCursor(
        final DailygeUser dailygeUser,
        final Cursor cursor,
        final LocalDateTime weekStartDate
    ) {
        return weeklyGoalReadDao.findByCursor(dailygeUser.getUserId(), cursor, weekStartDate);
    }
}
