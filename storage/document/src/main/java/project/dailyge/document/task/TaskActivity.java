package project.dailyge.document.task;

import lombok.Getter;

@Getter
public class TaskActivity {

    private Long userId;
    private String taskId;

    private TaskActivity() {
    }

    public boolean isOwner(final Long userId) {
        return this.userId.equals(userId);
    }
}
