package project.dailyge.app.core.monthlygoal.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.cursor.Cursor;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

import java.util.List;

public interface MonthlyGoalReadService {
    MonthlyGoalJpaEntity findById(Long monthlyGoalId);

    List<MonthlyGoalJpaEntity> findMonthlyGoalsByCursor(DailygeUser dailygeUser, Cursor cursor, Integer year, Integer month);
}
