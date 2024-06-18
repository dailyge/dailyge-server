package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.domain.task.TaskJpaEntity;

@Getter
public class TaskRegisterResponse {

    private Long taskId;

    private TaskRegisterResponse() {
    }

    public TaskRegisterResponse(final TaskJpaEntity task) {
        this.taskId = task.getId();
    }

    @Override
    public String toString() {
        return String.format("taskId: %s", taskId);
    }
}
