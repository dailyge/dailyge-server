package project.dailyge.document.task;

import lombok.Getter;

@Getter
public class TaskCount {
    private long count;

    private TaskCount() {
    }

    public TaskCount(final long count) {
        this.count = count;
    }
}
