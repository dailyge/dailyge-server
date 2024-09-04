package project.dailyge.app.core.monthlygoal.application.command;

public record MonthlyGoalUpdateCommand(
    String title,
    String content
) {

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : ""
        );
    }
}
