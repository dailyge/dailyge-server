package project.dailyge.dto.task;

import java.time.LocalDate;
import project.dailyge.entity.task.TaskStatus;

public record TaskStatisticDto(
    LocalDate date,
    TaskStatus status
) {
}
