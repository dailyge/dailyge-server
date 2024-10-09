package project.dailyge.app.core.weeklygoal.request;

import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;

public record WeeklyGoalUpdateRequest(
    String title,
    String content
) {

    public WeeklyGoalUpdateCommand toCommand() {
        return new WeeklyGoalUpdateCommand(title, content);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\"}",
            title, content
        );
    }
}
