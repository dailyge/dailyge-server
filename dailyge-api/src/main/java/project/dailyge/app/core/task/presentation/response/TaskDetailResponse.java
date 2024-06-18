package project.dailyge.app.core.task.dto.response;

import lombok.Getter;
import project.dailyge.domain.task.TaskJpaEntity;
import project.dailyge.domain.task.TaskStatus;

import java.time.LocalDate;

@Getter
public class TaskDetailResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDate date;
    private TaskStatus status;

    private TaskDetailResponse() {
    }

    public TaskDetailResponse(final TaskJpaEntity taskJpaEntity) {
        this.id = taskJpaEntity.getId();
        this.title = taskJpaEntity.getTitle();
        this.content = taskJpaEntity.getContent();
        this.date = taskJpaEntity.getDate();
        this.status = taskJpaEntity.getStatus();
    }

    @Override
    public String toString() {
        return String.format(
            "id:%s, title:%s, content:%s, date:%s, status:%s",
            id, title, content, date, status
        );
    }
}
