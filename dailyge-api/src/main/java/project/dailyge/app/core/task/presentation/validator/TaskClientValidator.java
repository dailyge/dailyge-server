package project.dailyge.app.core.task.presentation.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskClientValidator {

    public void validateWeeklyTasksSearchParams(
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("올바른 날짜를 입력해주세요.");
        }
    }
}
