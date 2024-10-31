package project.dailyge.app.core.task.presentation.response;

import project.dailyge.entity.task.TaskStatus;

public class TaskStatusResponse {

    private final String kr;
    private final String en;

    private TaskStatusResponse(final TaskStatus status) {
        this.kr = status.getKr();
        this.en = status.name();
    }

    public static TaskStatusResponse from(final TaskStatus status) {
        return new TaskStatusResponse(status);
    }

    public String getKr() {
        return kr;
    }

    public String getEn() {
        return en;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"kr\":\"%s\",\"en\":\"%s\"}",
            kr, en
        );
    }
}
