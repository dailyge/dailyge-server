package project.dailyge.entity.task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static project.dailyge.entity.task.RecurrenceType.DAILY;
import static project.dailyge.entity.task.RecurrenceType.MONTHLY;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;

public record RecurrenceTasks(
    TaskRecurrenceJpaEntity taskRecurrence,
    List<TaskJpaEntity> tasks,
    Long userId,
    TaskColor color,
    Map<YearMonth, Long> monthlyTaskMap
) {
    public List<TaskJpaEntity> create() {
        if (DAILY.equals(getRecurrenceType())) {
            final List<LocalDateTime> dailySchedules = generateDailySchedules();
            return createTasks(dailySchedules);
        }
        if (WEEKLY.equals(getRecurrenceType())) {
            final List<LocalDateTime> weeklySchedules = generateWeeklySchedules();
            return createTasks(weeklySchedules);
        }
        if (MONTHLY.equals(getRecurrenceType())) {
            final List<LocalDateTime> monthlySchedules = generateMonthlySchedules();
            return createTasks(monthlySchedules);
        }
        throw new IllegalArgumentException("유효하지 않은 반복 일정 유형입니다.");
    }

    private RecurrenceType getRecurrenceType() {
        return taskRecurrence.getRecurrenceType();
    }

    private List<TaskJpaEntity> createTasks(final List<LocalDateTime> schedules) {
        for (final LocalDateTime schedule : schedules) {
            TaskJpaEntity newTask = new TaskJpaEntity(
                taskRecurrence.getTitle(),
                taskRecurrence.getContent(),
                schedule.toLocalDate(),
                TaskStatus.TODO,
                color,
                Optional.ofNullable(monthlyTaskMap.get(YearMonth.of(schedule.getYear(),
                    schedule.getMonth()))).orElseThrow(() -> new IllegalArgumentException("해당 monthly task가 없습니다.")),
                userId,
                taskRecurrence.getId()
            );
            tasks.add(newTask);
        }
        return tasks;
    }

    private List<LocalDateTime> generateDailySchedules() {
        final List<LocalDateTime> dailySchedules = new ArrayList<>();
        LocalDateTime currentDate = taskRecurrence.getStartDate();
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            dailySchedules.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dailySchedules;
    }

    private List<LocalDateTime> generateWeeklySchedules() {
        final List<LocalDateTime> weeklySchedules = new ArrayList<>();
        LocalDateTime currentDate = taskRecurrence.getStartDate();
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            final DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            if (taskRecurrence.getDatePattern().contains(currentDayOfWeek.getValue())) {
                weeklySchedules.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        return weeklySchedules;
    }

    private List<LocalDateTime> generateMonthlySchedules() {
        LocalDateTime currentDate = taskRecurrence.getStartDate();
        final Set<LocalDateTime> monthlySchedules = new HashSet<>();
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            final YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
            int lastDayOfMonth = yearMonth.lengthOfMonth();
            for (final int day : taskRecurrence.getDatePattern()) {
                final int scheduleDay = Math.min(day, lastDayOfMonth);
                final LocalDateTime scheduleDate = LocalDateTime.of(
                    currentDate.getYear(),
                    currentDate.getMonth(),
                    scheduleDay,
                    currentDate.getHour(),
                    currentDate.getMinute()
                );
                if (!scheduleDate.isBefore(taskRecurrence.getStartDate()) && !scheduleDate.isAfter(taskRecurrence.getEndDate())) {
                    monthlySchedules.add(scheduleDate);
                }
            }
            currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
        }
        return new ArrayList<>(monthlySchedules);
    }
}
