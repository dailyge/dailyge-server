package project.dailyge.app.core.weeklygoal.application.command;

public record WeeklyGoalUpdateCommand(
    String title,
    String content
) {

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\"}",
            title, content
        );
    }
}
