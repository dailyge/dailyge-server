package project.dailyge.entity.task;

import java.util.ArrayList;
import java.util.List;

public record MonthlyTasks(List<MonthlyTaskJpaEntity> monthlyTasks) {

    public static List<MonthlyTaskJpaEntity> createMonthlyTasks(
        final Long userId,
        final int currentYear
    ) {
        final List<MonthlyTaskJpaEntity> monthlyTasks = new ArrayList<>();
        final int startYear = currentYear - 5;
        final int endYear = currentYear + 5;

        for (int year = startYear; year <= endYear; year++) {
            for (int month = 1; month <= 12; month++) {
                final MonthlyTaskJpaEntity monthlyTask = new MonthlyTaskJpaEntity(year, month, userId);
                monthlyTasks.add(monthlyTask);
            }
        }
        return monthlyTasks;
    }
}
