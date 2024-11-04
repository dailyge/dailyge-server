package project.dailyge.entity.test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.RecurrenceTasks;
import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] RecurrenceTasks 단위 테스트")
class RecurrenceTasksUnitTest {

    private final LocalDate startDate = LocalDate.of(2024, 10, 1);
    private final LocalDate endDate = LocalDate.of(2025, 9, 30);

    @Test
    @DisplayName("Daily 반복 일정 테스트")
    void whenTaskRecurrenceIsDailyThenTaskSizeShoulBeCorrect() {
        final TaskRecurrenceJpaEntity taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            RecurrenceType.DAILY,
            null,
            "수영",
            "오전 8:30 일정",
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0)
        );
        final TaskColor color = TaskColor.GRAY;
        final Map<YearMonth, Long> map = createMonthlyTaskMap(startDate, endDate);
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            1L,
            color,
            map
        );
        final List<TaskJpaEntity> tasks = recurrenceTasks.create();
        assertEquals(365, tasks.size());
    }

    Map<YearMonth, Long> createMonthlyTaskMap(LocalDate startDate, LocalDate endDate) {
        final Map<YearMonth, Long> monthlyTaskMap = new HashMap<>();
        YearMonth currentMonth = YearMonth.from(startDate);
        final YearMonth endMonth = YearMonth.from(endDate);
        long id = 1L;
        while (!currentMonth.isAfter(endMonth)) {
            monthlyTaskMap.put(currentMonth, id);
            currentMonth = currentMonth.plusMonths(1);
            id++;
        }
        return monthlyTaskMap;
    }
}
