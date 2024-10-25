package project.dailyge.entity.test.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;

@DisplayName("[UnitTest] 반복 일정 엔티티 테스트")
class TaskRecurrenceJpaEntityUnitTest {

    private TaskRecurrenceJpaEntity taskRecurrence;

    @BeforeEach
    void setUp() {
        final LocalDateTime now = LocalDateTime.now();
        taskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 TaskRecurrenceJpaEntity가 생성된다.")
    void whenValidArgumentsThenTaskRecurrenceJpaEntityShouldBeCreated() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskRecurrenceJpaEntity newTaskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
        assertAll(
            () -> assertThat(newTaskRecurrence.getId()).isEqualTo(1L),
            () -> assertThat(newTaskRecurrence.getTitle()).isEqualTo("수영"),
            () -> assertThat(newTaskRecurrence.getContent()).isEqualTo("자유형 배우기"),
            () -> assertThat(newTaskRecurrence.getDatePattern()).isEqualTo(List.of(1)),
            () -> assertThat(newTaskRecurrence.getRecurrenceType()).isEqualTo(WEEKLY),
            () -> assertThat(newTaskRecurrence.getStartDate()).isEqualTo(now),
            () -> assertThat(newTaskRecurrence.getEndDate()).isEqualTo(now.plusMonths(3))
        );
    }

    @Test
    @DisplayName("id가 동일하면 equals 반환 값이 True이다.")
    void whenIdIsSameThenEqualsShouldReturnTrue() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskRecurrenceJpaEntity newTaskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
        assertEquals(taskRecurrence, newTaskRecurrence);
    }

    @Test
    @DisplayName("id가 동일하지 않으면 equals 반환 값이 False이다.")
    void whenIdIsNotSameThenEqualsShouldReturnFalse() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskRecurrenceJpaEntity newTaskRecurrence = new TaskRecurrenceJpaEntity(
            2L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
        assertNotEquals(taskRecurrence, newTaskRecurrence);
    }

    @Test
    @DisplayName("id가 동일하면 해시코드가 동일하다.")
    void whenIdIsSameThenHashCodeShouldBeSame() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskRecurrenceJpaEntity newTaskRecurrence = new TaskRecurrenceJpaEntity(
            1L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
        assertEquals(taskRecurrence.hashCode(), newTaskRecurrence.hashCode());
    }

    @Test
    @DisplayName("id가 동일하지 않으면 해시코드 값이 동일하지 않다.")
    void whenIdIsNotSameThenHashCodeShouldNotBeSame() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskRecurrenceJpaEntity newTaskRecurrence = new TaskRecurrenceJpaEntity(
            2L,
            WEEKLY,
            List.of(1),
            "수영",
            "자유형 배우기",
            now,
            now.plusMonths(3)
        );
        assertNotEquals(taskRecurrence.hashCode(), newTaskRecurrence.hashCode());
    }
}
