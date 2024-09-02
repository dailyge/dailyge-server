package project.dailyge.app.core.monthlygoal.application.validator;

import org.springframework.stereotype.Component;

@Component
public class MonthlyGoalValidator {

    public void validateYearAndMonth(
        final Integer year,
        final Integer month
    ) {
        if (year == null || month == null) {
            throw new IllegalArgumentException("올바른 년/월을 입력해주세요.");
        }
        if (month <= 0 || month >= 13) {
            throw new IllegalArgumentException("올바른 년/월을 입력해주세요.");
        }
    }
}
