package project.dailyge.app.core.task.presentation.validator;

import static java.time.temporal.ChronoUnit.DAYS;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskClientValidator {

    private static final long MAX_DAYS_DIFFERENCE = 43;

    public void validateFromStartDateToEndDate(
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("올바른 날짜를 입력해주세요.");
        }

        final long daysDifference = DAYS.between(startDate, endDate);
        if (daysDifference > MAX_DAYS_DIFFERENCE) {
            throw new IllegalArgumentException("올바른 시작일과 종료일을 입력해 주세요.");
        }
    }

    public void validateOneMonthDifference(
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("올바른 날짜를 입력해주세요.");
        }
        if (ChronoUnit.MONTHS.between(startDate, endDate) != 1) {
            throw new IllegalArgumentException("올바른 시작일과 종료일을 입력해 주세요.");
        }
    }
}
