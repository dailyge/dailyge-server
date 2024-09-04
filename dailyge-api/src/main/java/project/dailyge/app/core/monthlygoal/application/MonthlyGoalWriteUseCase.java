package project.dailyge.app.core.monthlygoal.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalStatusUpdateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalUpdateCommand;

public interface MonthlyGoalWriteUseCase {
    Long save(DailygeUser dailygeUser, MonthlyGoalCreateCommand command);

    void delete(DailygeUser dailygeUser, Long monthlyGoalId);

    void update(DailygeUser dailygeUser, Long monthlyGoalId, MonthlyGoalUpdateCommand command);

    void update(DailygeUser dailygeUser, Long monthlyGoalId, MonthlyGoalStatusUpdateCommand command);
}
