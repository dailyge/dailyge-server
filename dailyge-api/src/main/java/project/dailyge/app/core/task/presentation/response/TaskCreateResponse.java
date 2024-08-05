package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

@Getter
public class TaskCreateResponse {

    private String taskId;

    private TaskCreateResponse() {
    }

    public TaskCreateResponse(final String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return String.format("{\"taskId\":\"%s\"}", taskId);
    }
}
