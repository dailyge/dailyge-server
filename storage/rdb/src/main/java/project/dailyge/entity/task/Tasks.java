package project.dailyge.entity.task;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import java.time.LocalDate;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<LocalDate, List<TaskJpaEntity>> monthTaskGroupByDate(final LocalDate monthLocalDate) {
        if (taskEntities.isEmpty()) {
            return new HashMap<>();
        }
        return taskEntities.stream().filter(task -> task.getDate().getMonth() == monthLocalDate.getMonth())
            .collect(groupingBy(task -> task.getDate()));
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

    public int size() {
        if (taskEntities.isEmpty()) {
            return 0;
        }
        return taskEntities.size();
    }
}
