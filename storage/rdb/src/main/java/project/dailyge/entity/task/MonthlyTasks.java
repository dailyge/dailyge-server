package project.dailyge.entity.task;

import java.util.ArrayList;
import java.util.List;

public record MonthlyTasks(List<MonthlyTaskJpaEntity> monthlyTasks) {

    public static List<MonthlyTaskJpaEntity> createMonthlyTasks(
        final Long userId,
        final int year
    ) {
        final List<MonthlyTaskJpaEntity> monthlyTasks = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            final MonthlyTaskJpaEntity monthlyTask = new MonthlyTaskJpaEntity(year, month, userId);
            monthlyTasks.add(monthlyTask);
        }
        return monthlyTasks;
    }
}
