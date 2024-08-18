package project.dailyge.app.core.monthly_goal.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalStatusUpdateCommand;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalUpdateCommand;

public interface MonthlyGoalWriteUseCase {
    Long save(DailygeUser dailygeUser, MonthlyGoalCreateCommand command);

    void delete(DailygeUser dailygeUser, Long monthlyGoalId);

    void update(DailygeUser dailygeUser, Long monthlyGoalId, MonthlyGoalUpdateCommand command);

    void update(DailygeUser dailygeUser, Long monthlyGoalId, MonthlyGoalStatusUpdateCommand command);
}
