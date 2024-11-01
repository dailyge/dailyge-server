package project.dailyge.app.core.task.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public final class ScheduleUtils {

    public static List<LocalDateTime> generateDailySchedules(
        final LocalDateTime startTime,
        final LocalDateTime endTime
    ) {
        final List<LocalDateTime> dailySchedules = new ArrayList<>();
        LocalDateTime currentDate = startTime;
        while (!currentDate.isAfter(endTime)) {
            dailySchedules.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dailySchedules;
    }

    public static List<LocalDateTime> generateWeeklySchedules(
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final List<Integer> days
    ) {
        final List<LocalDateTime> weeklySchedules = new ArrayList<>();
        LocalDateTime currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            if (days.contains(currentDayOfWeek.getValue())) {
                weeklySchedules.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        return weeklySchedules;
    }

    public static List<LocalDateTime> generateMonthlySchedules(
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final List<Integer> days
    ) {
        LocalDateTime currentDate = startDate;
        final List<LocalDateTime> monthlySchedules = new ArrayList<>();
        while (!currentDate.isAfter(endDate)) {
            final YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
            int lastDayOfMonth = yearMonth.lengthOfMonth();
            for (final int day : days) {
                final int scheduleDay = Math.min(day, lastDayOfMonth);
                final LocalDateTime scheduleDate = LocalDateTime.of(
                    currentDate.getYear(),
                    currentDate.getMonth(),
                    scheduleDay,
                    currentDate.getHour(),
                    currentDate.getMinute()
                );
                if (!scheduleDate.isBefore(startDate) && !scheduleDate.isAfter(endDate)) {
                    monthlySchedules.add(scheduleDate);
                }
            }
            currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
        }
        return monthlySchedules;
    }
}
