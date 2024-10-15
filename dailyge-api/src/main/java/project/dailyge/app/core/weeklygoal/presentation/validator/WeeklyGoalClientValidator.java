package project.dailyge.app.core.weeklygoal.presentation.validator;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class WeeklyGoalClientValidator {

    public void validateWeekStartDate(final LocalDate weekStartDate) {
        if (!DayOfWeek.MONDAY.equals(weekStartDate.getDayOfWeek())) {
            throw new IllegalArgumentException("한주의 시작이 월요일이 아닙니다.");
        }
    }
}
