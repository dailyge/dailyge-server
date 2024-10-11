package project.dailyge.app.core.weeklygoal.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;

public interface WeeklyGoalWriteService {
    Long save(DailygeUser dailygeUser, WeeklyGoalCreateCommand command);

    void update(DailygeUser dailygeUser, Long weeklyGoalId, WeeklyGoalUpdateCommand command);

    void update(DailygeUser dailygeUser, Long weeklyGoalId, boolean done);

    void delete(DailygeUser dailygeUser, Long weeklyGoalId);
}
