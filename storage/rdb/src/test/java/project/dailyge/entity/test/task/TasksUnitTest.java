package project.dailyge.entity.test.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;
import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.entity.task.TaskStatus.DONE;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;
import static project.dailyge.entity.task.TaskStatus.TODO;
import static project.dailyge.entity.task.Tasks.calculatePercentage;
import static project.dailyge.entity.task.Tasks.getWeekOfMonth;

@DisplayName("[UnitTest] Tasks 단위 테스트")
class TasksUnitTest {

    private List<TaskJpaEntity> tasks;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        today = now();
        tasks.add(new TaskJpaEntity("Task 1", "Content 1", today, TODO, 1L));
        tasks.add(new TaskJpaEntity("Task 2", "Content 2", today, TODO, 1L));
        tasks.add(new TaskJpaEntity("Task 3", "Content 3", today, TODO, 1L));
        tasks.add(new TaskJpaEntity("Task 4", "Content 4", today, IN_PROGRESS, 1L));
        tasks.add(new TaskJpaEntity("Task 5", "Content 5", today, IN_PROGRESS, 1L));
        tasks.add(new TaskJpaEntity("Task 6", "Content 6", today, IN_PROGRESS, 1L));
        tasks.add(new TaskJpaEntity("Task 7", "Content 7", today, DONE, 1L));
        tasks.add(new TaskJpaEntity("Task 8", "Content 8", today, DONE, 1L));
        tasks.add(new TaskJpaEntity("Task 9", "Content 9", today, DONE, 1L));
        tasks.add(new TaskJpaEntity("Task 10", "Content 10", today, DONE, 1L));
    }

    @AfterEach
    void clear() {
        tasks.clear();
    }

    @Test
    @DisplayName("TODO 상태 Task가 3개일 경우 달성률이 30%가 나온다.")
    void whenTasksHave30PercentTODOThenCalculatePercentageCorrectly() {
        final double result = calculatePercentage(tasks, TODO);
        assertEquals(30.0, result);
    }

    @Test
    @DisplayName("IN_PROGRESS 상태 Task가 3개일 경우 달성률이 30%가 나온다.")
    void whenTasksHave30PercentInProgressThenCalculatePercentageCorrectly() {
        final double result = calculatePercentage(tasks, IN_PROGRESS);
        assertEquals(30.0, result);
    }

    @Test
    @DisplayName("DONE 상태 Task가 4개일 경우 달성률이 40%가 나온다.")
    void whenTasksHave40PercentDoneThenCalculatePercentageCorrectly() {
        final double result = calculatePercentage(tasks, DONE);
        assertEquals(40.0, result);
    }

    @Test
    @DisplayName("Tasks가 비어있을 때 0%를 반환 한다.")
    void whenTaskListIsEmptyThenReturnZeroPercentage() {
        final List<TaskJpaEntity> newTasks = new ArrayList<>();
        final double result = calculatePercentage(newTasks, TODO);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Tasks의 크기를 반환할 수 있다.")
    void whenTasksArePresentThenReturnCorrectSize() {
        final Tasks taskWrapper = new Tasks(tasks);
        assertEquals(10, taskWrapper.size());
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우, 크기는 0이어야 한다.")
    void whenTaskListIsEmptyThenReturnSizeZero() {
        final Tasks taskWrapper = new Tasks(emptyList());
        assertEquals(0, taskWrapper.size());
    }

    @Test
    @DisplayName("Tasks를 날짜별로 그룹화할 수 있다.")
    void whenTasksAreGroupedByDateThenGroupCorrectly() {
        final Tasks taskWrapper = new Tasks(tasks);
        final Map<String, List<TaskJpaEntity>> groupedTasks = taskWrapper.groupByDate();
        assertEquals(1, groupedTasks.size());
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우 빈 그룹을 반환 한다.")
    void whenTaskListIsEmptyThenReturnEmptyGroup() {
        final Tasks taskWrapper = new Tasks(emptyList());
        final Map<String, List<TaskJpaEntity>> groupedTasks = taskWrapper.groupByDate();
        assertTrue(groupedTasks.isEmpty());
    }

    @Test
    @DisplayName("Tasks의 날짜 키를 문자열로 변환할 수 있다.")
    void whenGettingKeysAsStringThenReturnCorrectKeys() {
        final Tasks taskWrapper = new Tasks(tasks);
        final List<String> keys = taskWrapper.getKeysAsString();
        assertEquals(10, keys.size());
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우, 빈 문자열 목록을 반환해야 한다.")
    void whenGettingKeysAsStringWithEmptyListThenReturnEmptyKeys() {
        final Tasks taskWrapper = new Tasks(emptyList());
        final List<String> keys = taskWrapper.getKeysAsString();
        assertTrue(keys.isEmpty());
    }

    @Test
    @DisplayName("달성률이 0%일 경우 0을 반환 한다.")
    void whenNoTasksMatchThenReturnZeroPercentage() {
        final List<TaskJpaEntity> emptyTasks = emptyList();
        final double result = calculatePercentage(emptyTasks, TODO);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("특정 상태 Task 개수를 계산할 수 있다.")
    void whenTasksArePresentThenCalculateStatusCountCorrectly() {
        final long count = Tasks.calculateAchievementRate(tasks, TODO);
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Tasks가 빈 경우 0을 반환한다.")
    void whenTasksIsEmptyThanResultShouldBeZero() {
        final long count = Tasks.calculateAchievementRate(new ArrayList<>(), TODO);
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Tasks에 일치하는 상태가 없을 경우 0을 반환 한다.")
    void whenNoTasksMatchStatusThenReturnZeroStatusCount() {
        final long count = Tasks.calculateAchievementRate(tasks, null);
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Task 전체 수가 소수라도 정확한 결과가 나온다.")
    void whenTotalTaskCountIsPrimeThenResultShouldBeCorrectly() {
        final List<TaskJpaEntity> newTasks = new ArrayList<>();
        newTasks.add(new TaskJpaEntity("Task 1", "Content 1", now(), TODO, 1L));
        newTasks.add(new TaskJpaEntity("Task 2", "Content 2", now(), TODO, 1L));
        newTasks.add(new TaskJpaEntity("Task 3", "Content 3", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 4", "Content 4", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 5", "Content 5", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 6", "Content 6", now(), DONE, 1L));
        newTasks.add(new TaskJpaEntity("Task 7", "Content 7", now(), DONE, 1L));

        assertAll(
            () -> assertEquals(28.57, calculatePercentage(newTasks, TODO)),
            () -> assertEquals(42.86, calculatePercentage(newTasks, IN_PROGRESS)),
            () -> assertEquals(28.57, calculatePercentage(newTasks, DONE))
        );
    }

    @Test
    @DisplayName("Tasks에 3개의 TODO, IN_PROGRESS, DONE 작업이 있을 경우 각각 33.33%의 비율을 반환한다.")
    void whenTasksHaveEqualStatusThenReturnCorrectPercentage() {
        final List<TaskJpaEntity> newTasks = new ArrayList<>();
        newTasks.add(new TaskJpaEntity("Task 1", "Content 1", now(), TODO, 1L));
        newTasks.add(new TaskJpaEntity("Task 2", "Content 2", now(), TODO, 1L));
        newTasks.add(new TaskJpaEntity("Task 3", "Content 3", now(), TODO, 1L));
        newTasks.add(new TaskJpaEntity("Task 4", "Content 4", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 5", "Content 5", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 6", "Content 6", now(), IN_PROGRESS, 1L));
        newTasks.add(new TaskJpaEntity("Task 7", "Content 7", now(), DONE, 1L));
        newTasks.add(new TaskJpaEntity("Task 8", "Content 8", now(), DONE, 1L));
        newTasks.add(new TaskJpaEntity("Task 9", "Content 9", now(), DONE, 1L));

        assertAll(
            () -> assertEquals(33.33, calculatePercentage(newTasks, TODO)),
            () -> assertEquals(33.33, calculatePercentage(newTasks, IN_PROGRESS)),
            () -> assertEquals(33.33, calculatePercentage(newTasks, DONE))
        );
    }

    @Test
    @DisplayName("입력한 월의 Task를 반환 한다.")
    void whenGetMonthTaskThenResultShouldBeOneMonthTasks() {
        final TaskJpaEntity beforeMonthTask = new TaskJpaEntity("Task 11", "Content 11", today.minusMonths(1), TODO, 1L);
        tasks.add(beforeMonthTask);
        final Tasks taskWrapper = new Tasks(tasks);

        final Map<LocalDate, List<TaskJpaEntity>> groupedTasks = taskWrapper.groupByDateOfMonth(today);

        assertNotNull(groupedTasks.get(today));
        assertNull(groupedTasks.get(today.minusMonths(1)));
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우 빈 그룹을 반환 한다.")
    void whenGetMonthTaskListIsEmptyThenResultShouldBeEmptyGroup() {
        final Tasks taskWrapper = new Tasks(emptyList());
        final Map<LocalDate, List<TaskJpaEntity>> monthTaskMap = taskWrapper.groupByDateOfMonth(today);
        assertTrue(monthTaskMap.isEmpty());
    }

    @Test
    @DisplayName("해당 달의 주간 별 Task를 반환 한다.")
    void whenFindWeekTaskCountsThenResultShouldBeCorrectly() {
        final Tasks taskWrapper = new Tasks(tasks);
        final Map<Integer, List<TaskJpaEntity>> expectedTasks = new HashMap<>();
        expectedTasks.put(getWeekOfMonth(today) - 1, tasks);

        final Map<Integer, List<TaskJpaEntity>> groupedTasks = taskWrapper.groupByWeekOfMonth(today);

        assertEquals(expectedTasks, groupedTasks);
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우 빈 그룹을 반환 한다.")
    void whenFindWeekTaskCountsByEmptyTasksThenResultShouldBeCorrectly() {
        final Tasks taskWrapper = new Tasks(emptyList());

        final Map<Integer, List<TaskJpaEntity>> groupedTasks = taskWrapper.groupByWeekOfMonth(today);

        assertTrue(groupedTasks.isEmpty());
    }

    @Test
    @DisplayName("해당 달의 Rank별 Task 개수를 반환 한다.")
    void whenFindTaskRankCountsThenResultShouldBeCorrectly() {
        final Tasks taskWrapper = new Tasks(tasks);
        final List<Integer> expectedRanks = List.of(0, 0, 1, 0, 0);

        final List<Integer> rankCounts = taskWrapper.monthTaskRankCounts(today);

        assertEquals(expectedRanks, rankCounts);
    }

    @Test
    @DisplayName("Tasks가 비어있을 경우 빈 배열을 반환 한다.")
    void whenFindTaskRankCountsByEmptyTasksThenResultShouldBeEmptyList() {
        final Tasks taskWrapper = new Tasks(emptyList());
        final List<Integer> rankCounts = taskWrapper.monthTaskRankCounts(today);

        assertTrue(rankCounts.isEmpty());
    }

    @ParameterizedTest
    @DisplayName("월의 주차를 구하면, 올바른 값이 나온다.")
    @CsvSource({"1, 1", "2, 8", "3, 15", "4, 22", "5, 29"})
    void whenFindWeekOfMonthThenResultShouldBeCorrectly(
        final int expectedWeek,
        final int day
    ) {
        final LocalDate week = LocalDate.of(2024, 1, day);
        int weekOfMonth = getWeekOfMonth(week);

        assertEquals(expectedWeek, weekOfMonth);
    }

    @Test
    @DisplayName("목요일이 포함되지 않은 첫 주차는 1주차에 포함한다.")
    void whenFirstWeekNotIncludingThursdayThenResultShouldBeWeek1() {
        final LocalDate week = LocalDate.of(2024, 3, 1);
        int weekOfMonth = getWeekOfMonth(week);

        assertEquals(1, weekOfMonth);
    }

    @Test
    @DisplayName("랭크 별 백분율을 계산한다.")
    void whenCalculatePercentageByRankThenResultShouldBeCorrectly() {
        final List<Integer> rankCounts = List.of(1, 2, 3, 4, 5);
        final List<Double> expectedRates = List.of(6.67, 13.33, 20.0, 26.67, 33.33);
        final List<Double> rankRates = Tasks.calculateMonthlyRanks(rankCounts);

        assertEquals(expectedRates, rankRates);
    }

    @Test
    @DisplayName("Task가 비어있다면, 모두 0.0을 반환한다.")
    void whenGetThenResultShouldBeZero() {
        final List<Double> expectedRates = List.of(0.0, 0.0, 0.0, 0.0, 0.0);
        final List<Double> rankRates = Tasks.calculateMonthlyRanks(emptyList());

        assertEquals(expectedRates, rankRates);
    }
}
