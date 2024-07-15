package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

@Getter
public class TaskRegisterResponse {

    private String taskId;

    private TaskRegisterResponse() {
    }

    public TaskRegisterResponse(final String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return String.format("taskDocumentId: %s", taskId);
    }
}
