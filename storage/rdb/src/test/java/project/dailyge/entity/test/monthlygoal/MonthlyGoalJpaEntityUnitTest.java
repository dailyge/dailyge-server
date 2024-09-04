package project.dailyge.entity.test.monthlygoal;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.monthlygoal.MonthlyGoalFixture;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

@DisplayName("[UnitTest] 월간 목표 엔티티 테스트")
class MonthlyGoalJpaEntityUnitTest {

    private MonthlyGoalJpaEntity monthlyGoal;

    @BeforeEach
    void setUp() {
        monthlyGoal = MonthlyGoalFixture.newMonthlyGoal;
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 월간 목표가 생성된다.")
    void whenValidArgumentsProvidedThenMonthlyGoalIsCreated() {
        final MonthlyGoalJpaEntity newMonthlyGoal = monthlyGoal;

        assertAll(
            () -> assertThat(newMonthlyGoal.getId()).isEqualTo(1L),
            () -> assertThat(newMonthlyGoal.getTitle()).isEqualTo("달성 목표"),
            () -> assertThat(newMonthlyGoal.getContent()).isEqualTo("월간 목표 내용"),
            () -> assertThat(newMonthlyGoal.isDone()).isFalse(),
            () -> assertThat(newMonthlyGoal.getUserId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("기본 생성자를 통해 월간 목표를 생성할 수 있다.")
    void whenDefaultConstructorIsUsedThenMonthlyGoalHasDefaultValues() {
        final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity();

        assertAll(
            () -> assertThat(newMonthlyGoal.getId()).isNull(),
            () -> assertThat(newMonthlyGoal.getTitle()).isNull(),
            () -> assertThat(newMonthlyGoal.getContent()).isNull(),
            () -> assertThat(newMonthlyGoal.isDone()).isFalse(),
            () -> assertThat(newMonthlyGoal.getUserId()).isNull()
        );
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 월간 목표 제목과 내용이 업데이트된다.")
    void whenValidArgumentsProvidedThenMonthlyGoalIsUpdated() {
        final String updatedTitle = "업데이트된 목표";
        final String updatedContent = "업데이트된 내용";

        monthlyGoal.update(updatedTitle, updatedContent);

        assertAll(
            () -> assertThat(monthlyGoal.getTitle()).isEqualTo(updatedTitle),
            () -> assertThat(monthlyGoal.getContent()).isEqualTo(updatedContent)
        );
    }

    @Test
    @DisplayName("상태 값이 들어오면 월간 목표 상태가 변경된다.")
    void whenValidStatusProvidedThenMonthlyGoalStatusShouldBeUpdated() {
        monthlyGoal.updateStatus(true);
        assertTrue(monthlyGoal.isDone());
    }

    @Test
    @DisplayName("월간 목표를 삭제하면 상태가 바뀐다.")
    void whenDeleteThenStatusShouldBeChanged() {
        final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity(1L, "달성 목표", "월간 목표 내용", false, now(), 1L);
        newMonthlyGoal.delete();
        assertTrue(newMonthlyGoal.getDeleted());
    }

    @Test
    @DisplayName("ID가 같다면 같은 객체로 여긴다.")
    void whenIdIsSameThenInstancesAreEqual() {
        final MonthlyGoalJpaEntity expectedMonthlyGoal = new MonthlyGoalJpaEntity(
            1L,
            "달성 목표",
            "월간 목표 내용",
            false,
            now(),
            1L
        );
        final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity(
            1L,
            "달성 목표",
            "월간 목표 내용",
            false,
            now(),
            1L
        );

        assertThat(newMonthlyGoal).isEqualTo(expectedMonthlyGoal);
    }

    @Test
    @DisplayName("ID가 다르면 다른 객체로 여긴다.")
    void whenIdIsDifferentThenInstancesAreNotEqual() {
        final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity(
            1L,
            "달성 목표",
            "월간 목표 내용",
            false,
            now(),
            1L
        );
        final MonthlyGoalJpaEntity differentMonthlyGoal = new MonthlyGoalJpaEntity(
            2L,
            "달성 목표",
            "월간 목표 내용",
            false,
            now(),
            1L
        );

        assertThat(newMonthlyGoal).isNotEqualTo(differentMonthlyGoal);
    }

    @Test
    @DisplayName("ID가 같다면 해시코드가 동일하다.")
    void whenIdIsSameThenHashCodeIsSame() {
        final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity(
            1L,
            "달성 목표",
            "월간 목표 내용",
            false,
            now(),
            1L
        );
        assertEquals(newMonthlyGoal.hashCode(), newMonthlyGoal.hashCode());
    }
}
