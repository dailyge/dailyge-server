package project.dailyge.app.core.task.presentation.validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import static java.time.temporal.ChronoUnit.DAYS;

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
        final LocalDate oneMonthPlusDate = startDate.plusMonths(1);
        if (!oneMonthPlusDate.getMonth().equals(endDate.getMonth())) {
            throw new IllegalArgumentException("올바른 시작일과 종료일을 입력해 주세요.");
        }
    }

    public void validateHexColorCode(final String color) {
        for (int i = 0; i < color.length(); i++) {
            if (Character.digit(color.charAt(i), 16) == -1) {
                throw new IllegalArgumentException("올바른 색상을 입력해주세요.");
            }
        }
    }
}
