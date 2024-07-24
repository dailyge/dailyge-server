package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.entity.task.TaskStatus;

@Getter
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
}
