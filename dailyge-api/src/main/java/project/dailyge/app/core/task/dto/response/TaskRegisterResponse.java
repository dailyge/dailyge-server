package project.dailyge.app.core.task.dto.response;

import lombok.*;
import project.dailyge.domain.task.*;

@Getter
public class TaskRegisterResponse {

    private Long taskId;

    private TaskRegisterResponse() {
    }

    public TaskRegisterResponse(TaskJpaEntity task) {
        this.taskId = task.getId();
    }

    @Override
    public String toString() {
        return String.format("taskId: %s", taskId);
    }
}
