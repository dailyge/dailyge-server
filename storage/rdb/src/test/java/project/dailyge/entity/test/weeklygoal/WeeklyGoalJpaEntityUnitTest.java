package project.dailyge.entity.test.weeklygoal;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.weeklygoal.WeeklyGoalFixture;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

@DisplayName("[UnitTest] 주간 목표 엔티티 테스트")
class WeeklyGoalJpaEntityUnitTest {

    private WeeklyGoalJpaEntity weeklyGoal;

    @BeforeEach
    void setUp() {
        weeklyGoal = WeeklyGoalFixture.newWeeklyGoal;
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 주간 목표가 생성된다.")
    void whenValidArgumentsProvidedThenWeeklyGoalIsCreated() {
        final WeeklyGoalJpaEntity newWeeklyGoal = new WeeklyGoalJpaEntity(
            1L,
            "달성 주간 목표",
            "주간 목표 내용",
            now(),
            1L
        );

        assertAll(
            () -> assertThat(newWeeklyGoal.getId()).isEqualTo(1L),
            () -> assertThat(newWeeklyGoal.getTitle()).isEqualTo("달성 주간 목표"),
            () -> assertThat(newWeeklyGoal.getContent()).isEqualTo("주간 목표 내용"),
            () -> assertThat(newWeeklyGoal.isDone()).isFalse(),
            () -> assertThat(newWeeklyGoal.getUserId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("기본 생성자를 통해 주간 목표를 생성할 수 있다.")
    void whenDefaultConstructorIsUsedThenWeeklyGoalHasDefaultValues() {
        final WeeklyGoalJpaEntity newWeeklyGoal = WeeklyGoalFixture.defaultWeeklyGoal;

        assertAll(
            () -> assertThat(newWeeklyGoal.getId()).isNull(),
            () -> assertThat(newWeeklyGoal.getTitle()).isNull(),
            () -> assertThat(newWeeklyGoal.getContent()).isNull(),
            () -> assertThat(newWeeklyGoal.isDone()).isFalse(),
            () -> assertThat(newWeeklyGoal.getUserId()).isNull()
        );
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 주간 목표 제목과 내용이 업데이트된다.")
    void whenValidArgumentsProvidedThenWeeklyGoalIsUpdated() {
        final String updatedTitle = "업데이트된 목표";
        final String updatedContent = "업데이트된 내용";

        weeklyGoal.update(updatedTitle, updatedContent);

        assertAll(
            () -> assertThat(weeklyGoal.getTitle()).isEqualTo(updatedTitle),
            () -> assertThat(weeklyGoal.getContent()).isEqualTo(updatedContent)
        );
    }

    @ParameterizedTest
    @DisplayName("인자의 완료상태 여부가 주간 목표의 완료 상태가 업데이트 된다.")
    @ValueSource(booleans = {true, false})
    void whenUpdateDoneThenDoneShouldBeChanged(final boolean done) {
        weeklyGoal.updateDone(done);
        assertEquals(done, weeklyGoal.isDone());
    }

    @Test
    @DisplayName("주간 목표를 삭제하면 상태가 바뀐다.")
    void whenDeleteThenStatusShouldBeChanged() {
        weeklyGoal.delete();
        assertTrue(weeklyGoal.getDeleted());
    }

    @Test
    @DisplayName("ID가 같다면 같은 객체로 여긴다.")
    void whenIdIsSameThenInstancesAreEqual() {
        final WeeklyGoalJpaEntity expectedWeeklyGoal = new WeeklyGoalJpaEntity(
            1L,
            "달성 목표",
            "주간 목표 내용",
            now(),
            1L
        );
        assertThat(weeklyGoal).isEqualTo(expectedWeeklyGoal);
    }

    @Test
    @DisplayName("ID가 다르면 다른 객체로 여긴다.")
    void whenIdIsDifferentThenInstancesAreNotEqual() {
        final WeeklyGoalJpaEntity differentWeeklyGoal = new WeeklyGoalJpaEntity(
            2L,
            "달성 목표",
            "주간 목표 내용",
            now(),
            1L
        );
        assertThat(differentWeeklyGoal).isNotEqualTo(weeklyGoal);
    }

    @Test
    @DisplayName("ID가 같다면 해시코드가 동일하다.")
    void whenIdIsSameThenHashCodeIsSame() {
        final WeeklyGoalJpaEntity newWeeklyGoal = new WeeklyGoalJpaEntity(
            1L,
            "달성 목표",
            "주간 목표 내용",
            now(),
            1L
        );
        assertEquals(newWeeklyGoal.hashCode(), weeklyGoal.hashCode());
    }
}
