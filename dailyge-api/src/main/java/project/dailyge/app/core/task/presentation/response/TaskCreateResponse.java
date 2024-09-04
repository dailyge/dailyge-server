package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

@Getter
public class TaskCreateResponse {

    private Long taskId;

    private TaskCreateResponse() {
    }

    public TaskCreateResponse(final Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return String.format("{\"taskId\":\"%s\"}", taskId);
    }
}
