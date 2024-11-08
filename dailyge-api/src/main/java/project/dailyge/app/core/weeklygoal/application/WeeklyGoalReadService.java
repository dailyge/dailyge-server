package project.dailyge.app.core.weeklygoal.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface WeeklyGoalReadService {
    List<WeeklyGoalJpaEntity> findPageByCursor(DailygeUser dailygeUser, Cursor cursor, LocalDateTime startWeekDate);
}
