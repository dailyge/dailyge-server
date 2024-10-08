package project.dailyge.app.core.anniversary.presentation.validator;

import static java.time.temporal.ChronoUnit.DAYS;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnniversaryClientValidator {

    private static final long MAX_DAYS_DIFFERENCE = 43;

    public void validateFromStartDateToEndDate(
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("날짜를 입력해주세요.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("올바른 날짜를 입력해주세요.");
        }

        final long daysDifference = DAYS.between(startDate, endDate);
        if (daysDifference > MAX_DAYS_DIFFERENCE) {
            throw new IllegalArgumentException("올바른 시작일과 종료일을 입력해 주세요.");
        }
    }
}
