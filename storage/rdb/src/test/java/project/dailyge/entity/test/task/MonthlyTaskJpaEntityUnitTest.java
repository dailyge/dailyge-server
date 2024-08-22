package project.dailyge.entity.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;

@DisplayName("[UnitTest] MonthlyTaskJpaEntity 테스트")
class MonthlyTaskJpaEntityUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 MonthlyTaskJpaEntity가 생성된다.")
    void whenValidArgumentsThenMonthlyTaskJpaEntityShouldBeCreated() {
        final Long userId = 1L;
        final int year = 2024;
        final int month = 5;
        final MonthlyTaskJpaEntity monthlyTask = new MonthlyTaskJpaEntity(year, month, userId);
        assertAll(
            () -> assertThat(monthlyTask.getId()).isNull(),
            () -> assertThat(monthlyTask.getYear()).isEqualTo(year),
            () -> assertThat(monthlyTask.getMonth()).isEqualTo(month),
            () -> assertThat(monthlyTask.getUserId()).isEqualTo(userId)
        );
    }

    @Test
    @DisplayName("올바른 id, year, month, userId로 MonthlyTaskJpaEntity를 생성할 수 있다.")
    void whenValidIdAndArgumentsThenMonthlyTaskJpaEntityShouldBeCreated() {
        final Long id = 1L;
        final Long userId = 1L;
        final int year = 2024;
        final int month = 5;
        final MonthlyTaskJpaEntity monthlyTask = new MonthlyTaskJpaEntity(id, year, month, userId);
        assertAll(
            () -> assertThat(monthlyTask.getId()).isEqualTo(id),
            () -> assertThat(monthlyTask.getYear()).isEqualTo(year),
            () -> assertThat(monthlyTask.getMonth()).isEqualTo(month),
            () -> assertThat(monthlyTask.getUserId()).isEqualTo(userId)
        );
    }

    @Test
    @DisplayName("년도가 1보다 작으면 IllegalArgumentException이 발생한다.")
    void whenYearIsLessThanOneThenIllegalArgumentExceptionShouldBeThrown() {
        final Long userId = 1L;
        final int invalidYear = 0;
        final int month = 5;
        assertThatThrownBy(() -> new MonthlyTaskJpaEntity(invalidYear, month, userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 년도를 입력해주세요.");
    }

    @Test
    @DisplayName("월이 1보다 작거나 12보다 크면 IllegalArgumentException이 발생한다.")
    void whenMonthIsInvalidThenIllegalArgumentExceptionShouldBeThrown() {
        final Long userId = 1L;
        final int year = 2024;
        final int invalidMonthLow = 0;
        final int invalidMonthHigh = 13;
        assertAll(
            () -> assertThatThrownBy(() -> new MonthlyTaskJpaEntity(year, invalidMonthLow, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바른 년도를 입력해주세요."),
            () -> assertThatThrownBy(() -> new MonthlyTaskJpaEntity(year, invalidMonthHigh, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바른 년도를 입력해주세요.")
        );
    }

    @Test
    @DisplayName("userId가 null이면 IllegalArgumentException이 발생한다.")
    void whenUserIdIsNullThenIllegalArgumentExceptionShouldBeThrown() {
        final int year = 2024;
        final int month = 5;
        assertThatThrownBy(() -> new MonthlyTaskJpaEntity(year, month, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("사용자 ID를 입력해주세요.");
    }
}
