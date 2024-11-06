package project.dailyge.entity.test.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.task.RecurrenceTasks;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static project.dailyge.entity.task.RecurrenceType.DAILY;
import static project.dailyge.entity.task.RecurrenceType.MONTHLY;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;

@DisplayName("[UnitTest] RecurrenceTasks 단위 테스트")
class RecurrenceTasksUnitTest {

    private LocalDate startDate;
    private LocalDate endDate;
    private Map<YearMonth, Long> monthlyTasksMap;
    private TaskColor color;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 10, 31);
        endDate = LocalDate.of(2025, 10, 30);
        monthlyTasksMap = createMonthlyTaskMap(startDate, endDate);
        color = TaskColor.GRAY;
    }

    @Test
    @DisplayName("Daily 반복 일정을 올바르게 생성한다.")
    void whenTaskRecurrenceIsDailyThenTaskSizeShouldBeCorrect() {
        final TaskRecurrenceJpaEntity taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            DAILY,
            null,
            "수영",
            "오전 8:30 일정",
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0)
        );
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            1L,
            color,
            monthlyTasksMap
        );
        final List<TaskJpaEntity> tasks = recurrenceTasks.create();
        assertEquals(365, tasks.size());
        tasks.forEach(task -> assertEquals(monthlyTasksMap.get(YearMonth.of(task.getYear(), task.getMonth())), task.getMonthlyTaskId()));
    }

    @ParameterizedTest
    @DisplayName("Weekly 반복 일정을 올바르게 생성한다.")
    @CsvSource({
        "1, 52",
        "2, 52",
        "3, 52",
        "4, 53",
        "5, 52",
        "6, 52",
        "7, 52",
    })
    void whenTaskRecurrenceIsWeeklyThenTaskSizeShouldBeCorrect(final int dayOfWeek, final int result) {
        final TaskRecurrenceJpaEntity taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            WEEKLY,
            List.of(dayOfWeek),
            "영어",
            "전화 영어 아무때나",
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0)
        );
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            1L,
            color,
            monthlyTasksMap
        );
        final List<TaskJpaEntity> tasks = recurrenceTasks.create();
        assertEquals(result, tasks.size());
        tasks.forEach(task -> assertEquals(monthlyTasksMap.get(YearMonth.of(task.getYear(), task.getMonth())), task.getMonthlyTaskId()));
    }

    @ParameterizedTest
    @DisplayName("Monthly 반복 일정을 올바르게 생성한다.")
    @ValueSource(ints = {1, 2, 4, 10, 11, 28, 29, 30, 31})
    void whenTaskRecurrenceIsMonthlyThenTaskSizeShouldBeCorrect(final int monthDay) {
        final TaskRecurrenceJpaEntity taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            MONTHLY,
            List.of(monthDay),
            "제빵 클래스",
            "나만의 빵을 만들자",
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0)
        );
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            1L,
            color,
            monthlyTasksMap
        );
        final List<TaskJpaEntity> tasks = recurrenceTasks.create();
        assertEquals(12, tasks.size());
        tasks.forEach(task -> assertEquals(monthlyTasksMap.get(YearMonth.of(task.getYear(), task.getMonth())), task.getMonthlyTaskId()));
    }

    @Test
    @DisplayName("MonthlyTaskMap이 유효하지 않으면 예외를 던진다.")
    void whenMonthlyTaskMapIsInvalidThenThrowIllegalArgumentException() {
        final TaskRecurrenceJpaEntity taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            MONTHLY,
            List.of(30),
            "제빵 클래스",
            "나만의 빵을 만들자",
            startDate.atTime(0, 0, 0, 0),
            endDate.atTime(0, 0, 0, 0)
        );
        Map<YearMonth, Long> invalidMonthlyTasksMap = new HashMap<>();
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            1L,
            color,
            invalidMonthlyTasksMap
        );
        assertThrows(IllegalArgumentException.class, () -> recurrenceTasks.create());
    }

    private Map<YearMonth, Long> createMonthlyTaskMap(final LocalDate startDate, final LocalDate endDate) {
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
