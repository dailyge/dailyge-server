package project.dailyge.entity.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.MonthlyTasks;

import java.util.List;

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
        assertAll(
            () -> assertThat(monthlyTasks).hasSize(12),
            () -> assertThat(monthlyTasks.get(0).getUserId()).isEqualTo(userId),
            () -> assertThat(monthlyTasks.get(0).getYear()).isEqualTo(year),
            () -> assertThat(monthlyTasks.get(0).getMonth()).isEqualTo(1),
            () -> assertThat(monthlyTasks.get(11).getMonth()).isEqualTo(12)
        );
    }

    @Test
    @DisplayName("생성된 MonthlyTask의 month 값이 1에서 12까지 순서대로 할당된다.")
    void whenCreateMonthlyTasksThenMonthsAreSequential() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertAll(
            () -> assertThat(monthlyTasks).hasSize(12),
            () -> {
                for (int month = 0; month <= 11; month++) {
                    assertThat(monthlyTasks.get(month).getMonth()).isEqualTo(month + 1);
                }
            }
        );
    }

    @Test
    @DisplayName("생성된 MonthlyTask의 userId와 year 값이 올바르게 할당된다.")
    void whenCreateMonthlyTasksThenUserIdAndYearAreCorrect() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertAll(
            () -> assertThat(monthlyTasks).allMatch(task -> task.getUserId().equals(userId)),
            () -> assertThat(monthlyTasks).allMatch(task -> task.getYear() == year)
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
    @DisplayName("12개의 MonthlyTaskJpaEntity 객체가 생성된다.")
    void whenCreateMonthlyTasksThenCorrectNumberOfTasksShouldBeCreated() {
        final List<MonthlyTaskJpaEntity> monthlyTasks = MonthlyTasks.createMonthlyTasks(userId, year);
        assertThat(monthlyTasks).hasSize(12);
    }
}
