package project.dailyge.entity.task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static final int FIRST_DAY = 1;
    private static final int ONE_DAY = 1;
    private static final int ONE_MONTH = 1;
    private static final int FIRST_INDEX = 0;

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
            final TaskJpaEntity newTask = createTask(schedule);
            tasks.add(newTask);
        }
        return tasks;
    }

    private TaskJpaEntity createTask(final LocalDateTime schedule) {
        final Long monthlyTaskId = getMonthlyTaskId(schedule);
        return new TaskJpaEntity(
            taskRecurrence.getTitle(),
            taskRecurrence.getContent(),
            schedule.toLocalDate(),
            TaskStatus.TODO,
            color,
            monthlyTaskId,
            userId,
            taskRecurrence.getId()
        );
    }

    private Long getMonthlyTaskId(final LocalDateTime schedule) {
        final Long monthlyTaskId = monthlyTaskMap.get(YearMonth.of(schedule.getYear(), schedule.getMonth()));
        if (monthlyTaskId == null) {
            throw new IllegalArgumentException("해당 monthly task가 없습니다.");
        }
        return monthlyTaskId;
    }

    private List<LocalDateTime> generateDailySchedules() {
        final List<LocalDateTime> dailySchedules = new ArrayList<>();
        LocalDateTime currentDate = taskRecurrence.getStartDate();
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            dailySchedules.add(currentDate);
            currentDate = currentDate.plusDays(ONE_DAY);
        }
        return dailySchedules;
    }

    private List<LocalDateTime> generateWeeklySchedules() {
        final List<LocalDateTime> weeklySchedules = new ArrayList<>();
        LocalDateTime currentDate = taskRecurrence.getStartDate();
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            if (matchesDatePattern(currentDate)) {
                weeklySchedules.add(currentDate);
            }
            currentDate = currentDate.plusDays(ONE_DAY);
        }
        return weeklySchedules;
    }

    private boolean matchesDatePattern(final LocalDateTime currentDate) {
        final DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        return taskRecurrence.getDatePattern().contains(currentDayOfWeek.getValue());
    }

    private List<LocalDateTime> generateMonthlySchedules() {
        LocalDateTime currentDate = taskRecurrence.getStartDate().withDayOfMonth(FIRST_DAY);
        final List<LocalDateTime> monthlySchedules = new ArrayList<>();
        final int recurrenceDay = taskRecurrence.getDatePattern().get(FIRST_INDEX);
        while (!currentDate.isAfter(taskRecurrence.getEndDate())) {
            final int scheduleDay = getMaxDayForMonth(recurrenceDay, currentDate);
            final LocalDateTime scheduleDate = currentDate.withDayOfMonth(scheduleDay);
            if (isWithinRange(scheduleDate)) {
                monthlySchedules.add(scheduleDate);
            }
            currentDate = currentDate.plusMonths(ONE_MONTH);
        }
        return monthlySchedules;
    }

    private int getMaxDayForMonth(
        final int recurrenceDay,
        final LocalDateTime currentDate
    ) {
        final YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        final int lastDayOfMonth = yearMonth.lengthOfMonth();
        return Math.min(recurrenceDay, lastDayOfMonth);
    }

    private boolean isWithinRange(final LocalDateTime scheduleDate) {
        return !scheduleDate.isBefore(taskRecurrence.getStartDate())
            && !scheduleDate.isAfter(taskRecurrence.getEndDate());
    }
}
