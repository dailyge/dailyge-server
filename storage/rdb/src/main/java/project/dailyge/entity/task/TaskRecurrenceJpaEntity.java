package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "task_recurrences")
public class TaskRecurrenceJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public TaskRecurrenceJpaEntity(
        final Long id,
        final String cronExpression,
        final String title,
        final String content,
        final LocalDateTime startDate,
        final LocalDateTime endDate
    ) {
        this.id = id;
        this.cronExpression = cronExpression;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
