package project.dailyge.entity.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.MonthlyTasks;

import java.util.List;
import java.util.function.Predicate;

@DisplayName("[UnitTest] MonthlyTasks 테스트")
class MonthlyTasksUnitTest {

    private Long userId;
    private int year;

    @BeforeEach
    void setUp() {
        userId = 1L;
        year = 2024;
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 MonthlyTasks가 올바르게 생성된다.")
    void whenValidArgumentsThenMonthlyTasksShouldBeCreated() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertEquals(132, monthlyTasks.size());
    }

    @Test
    @DisplayName("5년 전부터 5년 후까지의 모든 년도가 포함된다.")
    void whenYearIsProvidedThenMonthlyTasksIncludeAllYearsWithinRange() {
        final int startYear = year - 5;
        final int endYear = year + 5;
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);

        for (int year = startYear; year <= endYear; year++) {
            for (int month = 1; month <= 12; month++) {
                boolean exists = monthlyTasks.stream().anyMatch(equals(year, month));
                assertTrue(exists);
            }
        }
    }

    private Predicate<MonthlyTaskJpaEntity> equals(
        final int finalYear,
        final int finalMonth
    ) {
        return task -> task.getYear() == finalYear
            && task.getMonth() == finalMonth
            && task.getUserId().equals(userId);
    }

    @Test
    @DisplayName("생성된 MonthlyTask의 userId 값이 올바르게 할당된다.")
    void whenCreateMonthlyTasksThenUserIdShouldBeCorrected() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertAll(
            () -> assertThat(monthlyTasks).allMatch(task -> task.getUserId().equals(userId))
        );
    }

    @Test
    @DisplayName("year 값이 올바르지 않다면 예외가 발생한다.")
    void whenYearIsValidThenNoExceptionThrown() {
        final int year = 0;
        assertThatThrownBy(() -> MonthlyTasks.createMonthlyTasks(userId, year))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("132개의 MonthlyTaskJpaEntity 객체가 생성된다.")
    void whenCreateMonthlyTasksThenCorrectNumberOfTasksShouldBeCreated() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertThat(monthlyTasks).hasSize(132);
    }
}
