package project.dailyge.document.task;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class MonthlyTaskDocuments {

    private static final int START = 1;
    private static final int END = 12;

    private final List<MonthlyTaskDocument> tasks;

    public MonthlyTaskDocuments(final List<MonthlyTaskDocument> tasks) {
        this.tasks = tasks;
    }

    public static List<MonthlyTaskDocument> createMonthlyDocuments(
        final Long userId,
        final LocalDate date
    ) {
        final List<MonthlyTaskDocument> monthlyTasks = new ArrayList<>();
        for (int month = START; month <= END; month++) {
            final MonthlyTaskDocument monthlyTask = createMonthlyTaskDocument(userId, date.getYear(), month);
            monthlyTasks.add(monthlyTask);
        }
        return monthlyTasks;
    }

    private static MonthlyTaskDocument createMonthlyTaskDocument(
        final Long userId,
        final int year,
        final int month
    ) {
        return MonthlyTaskDocument.createMonthlyDocument(userId, year, month);
    }
}
