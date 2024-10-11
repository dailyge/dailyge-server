package project.dailyge.app.core.weeklygoal.application.validator;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class WeeklyGoalValidator {

    public void validateWeekStartDate(final LocalDateTime weekStartDate) {
        if (!weekStartDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            throw new IllegalArgumentException("한주의 시작이 월요일이 아닙니다.");
        }
    }
}
