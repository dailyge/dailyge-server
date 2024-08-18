package project.dailyge.app.core.monthly_goal.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalCreateCommand;

public interface MonthlyGoalWriteUseCase {
    Long save(DailygeUser dailygeUser, MonthlyGoalCreateCommand command);
}
