package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.util.List;

public record TaskRecurrenceCreateCommand(
    String title,
    String content,
    TaskColor color,
    RecurrenceType type,
    List<Integer> datePattern,
    LocalDate startDate,
    LocalDate endDate,
    Long userId
) {
    public TaskRecurrenceJpaEntity toEntity() {
        return new TaskRecurrenceJpaEntity(
            type,
            datePattern,
            title,
            content,
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0),
            userId
        );
    }
}
