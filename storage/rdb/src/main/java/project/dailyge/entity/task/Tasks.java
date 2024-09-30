package project.dailyge.entity.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static project.dailyge.entity.task.TaskAchievementRank.getAchievementRank;

@Getter
public final class Tasks {

    private static final int PERCENTAGE = 100;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<TaskJpaEntity> taskEntities;

    public Tasks(final List<TaskJpaEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public Map<String, List<TaskJpaEntity>> groupByDate() {
        if (taskEntities.isEmpty()) {
            return new HashMap<>();
        }
        return taskEntities.stream()
            .collect(groupingBy(task -> task.getDate().format(formatter)));
    }

    public Map<LocalDate, List<TaskJpaEntity>> groupByDateOfMonth(final LocalDate monthLocalDate) {
        if (taskEntities.isEmpty()) {
            return new HashMap<>();
        }
        return taskEntities.stream().filter(task -> task.getDate().getMonth() == monthLocalDate.getMonth())
            .collect(groupingBy(task -> task.getDate()));
    }

    public Map<Integer, List<TaskJpaEntity>> groupByWeekOfMonth(final LocalDate monthLocalDate) {
        final Map<LocalDate, List<TaskJpaEntity>> monthTaskMap = groupByDateOfMonth(monthLocalDate);

        final Map<Integer, List<TaskJpaEntity>> tasksGroupByWeekMap = new HashMap<>();
        for (final LocalDate date : monthTaskMap.keySet()) {
            final int weekOfMonth = getWeekOfMonth(date) - 1;
            tasksGroupByWeekMap.computeIfAbsent(weekOfMonth, k -> new ArrayList<>()).addAll(monthTaskMap.get(date));
        }
        return tasksGroupByWeekMap;
    }

    public List<Integer> monthTaskRankCounts(final LocalDate monthLocalDate) {
        final Map<LocalDate, List<TaskJpaEntity>> monthlyTaskMap = groupByDateOfMonth(monthLocalDate);
        if (monthlyTaskMap.isEmpty()) {
            return emptyList();
        }
        final List<Integer> rankCounts = Arrays.asList(0, 0, 0, 0, 0);
        for (final LocalDate date : monthlyTaskMap.keySet()) {
            final List<TaskJpaEntity> tasksByDate = monthlyTaskMap.get(date);
            final double successRateByDate = calculatePercentage(tasksByDate, TaskStatus.DONE);
            final int achievementRank = getAchievementRank(successRateByDate);
            rankCounts.set(achievementRank, rankCounts.get(achievementRank) + 1);
        }
        return rankCounts;
    }

    public List<String> getKeysAsString() {
        if (taskEntities.isEmpty()) {
            return emptyList();
        }
        return taskEntities.stream()
            .map(TaskJpaEntity::getDate)
            .map(String::valueOf)
            .toList();
    }

    public static double calculatePercentage(
        final long statusCount,
        final long totalTasks
    ) {
        if (totalTasks == 0) {
            return 0;
        }
        return Math.round(((double) statusCount / totalTasks) * PERCENTAGE * 100) / 100.0;
    }

    public static double calculatePercentage(
        final List<TaskJpaEntity> tasks,
        final TaskStatus status
    ) {
        final long totalTasks = tasks.size();
        if (totalTasks == 0) {
            return 0;
        }
        final long statusCount = calculateAchievementRate(tasks, status);
        return Math.round(((double) statusCount / totalTasks) * PERCENTAGE * 100) / 100.0;
    }

    public static long calculateAchievementRate(
        final List<TaskJpaEntity> tasks,
        final TaskStatus status
    ) {
        if (tasks.isEmpty()) {
            return 0L;
        }
        return tasks.stream()
            .filter(task -> task.getStatus() == status)
            .count();
    }

    public static List<Double> calculateMonthlyRanks(final List<Integer> rankCounts) {
        final List<Double> monthlyRanks = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        if (rankCounts.isEmpty()) {
            return monthlyRanks;
        }
        final int monthlySum = rankCounts.stream().mapToInt(i -> i).sum();
        for (int i = rankCounts.size() - 1; i > 0; i--) {
            monthlyRanks.set(i, calculatePercentage(rankCounts.get(i), monthlySum));
        }
        final Double lastRank = 100.0 - monthlyRanks.stream().mapToDouble(Double::doubleValue).sum();
        final BigDecimal roundedLastRank = new BigDecimal(lastRank).setScale(2, RoundingMode.HALF_UP);
        monthlyRanks.set(0, roundedLastRank.doubleValue());
        return monthlyRanks;
    }

    public static int getWeekOfMonth(final LocalDate date) {
        final WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        final int weekOfMonth = date.get(weekFields.weekOfMonth());
        return weekOfMonth == 0 ? 1 : weekOfMonth;
    }

    public int size() {
        if (taskEntities.isEmpty()) {
            return 0;
        }
        return taskEntities.size();
    }
}
