package project.dailyge.app.core.weeklygoal.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;

public interface WeeklyGoalWriteService {
    Long save(DailygeUser dailygeUser, WeeklyGoalCreateCommand command);
}
