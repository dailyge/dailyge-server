package project.dailyge.app.core.task.presentation.validator;

import org.springframework.stereotype.Component;
import project.dailyge.entity.task.RecurrenceType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static project.dailyge.entity.task.RecurrenceType.DAILY;
import static project.dailyge.entity.task.RecurrenceType.MONTHLY;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;

@Component
public class TaskRecurrenceClientValidator {

    private static final int FIRST_DAY = 1;
    private static final int LAST_DAY = 31;

    public void validateStartDateToEndDate(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜가 종료날짜보다 뒤에 있습니다.");
        }
        final long durationDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (durationDays > 365) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜 간기간이 1년이 넘습니다.");
        }
    }

    public void validateDayPattern(final RecurrenceType type, final List<Integer> dayPattern) {
        if (DAILY.equals(type) && dayPattern != null) {
            throw new IllegalArgumentException("일간 반복 일정에 맞지 않는 dayPattern 입니다.");
        }
        if (WEEKLY.equals(type) && !isWeeklyDayPattern(dayPattern)) {
            throw new IllegalArgumentException("주간 반복 일정에 맞지 않는 dayPattern 입니다.");
        }
        if (MONTHLY.equals(type) && !isMonthlyDayPattern(dayPattern)) {
            throw new IllegalArgumentException("월간 반복 일정에 맞지 않는 dayPattern 입니다.");
        }
    }

    private boolean isWeeklyDayPattern(final List<Integer> dayPattern) {
        return isSorted(dayPattern) && isBetween(dayPattern, MONDAY.getValue(), SUNDAY.getValue());
    }

    private boolean isMonthlyDayPattern(final List<Integer> dayPattern) {
        return isSorted(dayPattern) && isBetween(dayPattern, FIRST_DAY, LAST_DAY);
    }

    private boolean isSorted(final List<Integer> dayPattern) {
        return dayPattern != null && dayPattern.equals(dayPattern.stream().sorted().toList());
    }

    private boolean isBetween(final List<Integer> dayPattern, final int start, final int end) {
        return dayPattern.stream().allMatch(day -> day >= start && day <= end);
    }
}
