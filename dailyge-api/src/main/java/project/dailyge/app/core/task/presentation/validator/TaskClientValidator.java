package project.dailyge.app.core.task.presentation.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskClientValidator {

    public void validateWeeklyTasksSearchParams(
        final List<Long> monthlyTaskIds,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        if (monthlyTaskIds.isEmpty()) {
            throw new IllegalArgumentException("monthlyTaskId를 입력해주세요.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("올바른 날짜를 입력해주세요.");
        }
    }
}
