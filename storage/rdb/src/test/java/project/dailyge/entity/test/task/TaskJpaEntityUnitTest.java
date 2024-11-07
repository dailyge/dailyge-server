package project.dailyge.entity.test.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.entity.task.TaskStatus.TODO;

@DisplayName("[UnitTest] 할 일 엔티티 테스트")
class TaskJpaEntityUnitTest {

    private TaskJpaEntity task;

    @BeforeEach
    void setUp() {
        final LocalDateTime now = LocalDateTime.now();
        task = new TaskJpaEntity(
            1L,
            "프로젝트 관리",
            "프로젝트 진행 상황 점검",
            LocalDate.now().plusDays(10),
            TaskStatus.TODO,
            1L,
            1L,
            null,
            now,
            1L,
            now,
            1L,
            false
        );
    }

    @Test
    @DisplayName("올바른 인자가 들어오면 할 일이 생성된다.")
    void taskCreateTest() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "프로젝트 관리",
            "프로젝트 진행 상황 점검",
            LocalDate.now().plusDays(10),
            TaskStatus.TODO,
            1L,
            1L,
            null,
            now,
            1L,
            now,
            1L,
            false
        );

        assertAll(
            () -> assertThat(newTask.getId()).isEqualTo(1L),
            () -> assertThat(newTask.getTitle()).isEqualTo("프로젝트 관리"),
            () -> assertThat(newTask.getContent()).isEqualTo("프로젝트 진행 상황 점검"),
            () -> assertThat(newTask.getDate()).isEqualTo(LocalDate.now().plusDays(10)),
            () -> assertThat(newTask.getStatus()).isEqualTo(TODO),
            () -> assertThat(newTask.getUserId()).isEqualTo(1L),
            () -> assertThat(newTask.getCreatedAt()).isEqualTo(now),
            () -> assertThat(newTask.getCreatedBy()).isEqualTo(1L),
            () -> assertThat(newTask.getLastModifiedAt()).isEqualTo(now),
            () -> assertThat(newTask.getLastModifiedBy()).isEqualTo(1L),
            () -> assertThat(newTask.getDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("올바른 일정이 생성되면 예외가 발생하지 않는다.")
    void whenValidTaskThenShouldNotThrow() {
        final LocalDate futureDate = LocalDate.now().plusMonths(6);
        assertThatCode(() -> new TaskJpaEntity(
            "유효한 제목",
            "유효한 내용",
            futureDate,
            TODO,
            1L
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입력 가능한 최대 제목 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenTitleIsTooLongThenIllegalArgumentExceptionShouldBeOccur() {
        final String longTitle = "t".repeat(151);
        final LocalDate futureDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() ->
            new TaskJpaEntity(
                longTitle,
                "내용",
                futureDate,
                TODO,
                1L
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(task.getOverMaxTitleLengthErrorMessage());
    }

    @Test
    @DisplayName("입력 가능한 내용의 최대 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenContentIsTooLongThenIllegalArgumentExceptionShouldBeOccur() {
        final String longContent = "c".repeat(2501);
        final LocalDate futureDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() ->
            new TaskJpaEntity(
                "제목",
                longContent,
                futureDate,
                TODO,
                1L
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(task.getOverMaxContentLengthErrorMessage());
    }

    @Test
    @DisplayName("5년 전 날짜를 입력하면 IllegalArgumentException가 발생한다.")
    void whenDateIsBeforeThenIllegalArgumentExceptionShouldBeOccur() {
        final LocalDate pastDate = LocalDate.now().minusYears(6);

        assertThatThrownBy(() ->
            new TaskJpaEntity(
                "제목",
                "내용",
                pastDate,
                TODO,
                1L
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(task.getDateErrorMessage());
    }

    @Test
    @DisplayName("5년 초과 날짜 사용 시 IllegalArgumentException가 발생한다.")
    void whenDateIsTooFarThenIllegalArgumentExceptionShouldBeOccur() {
        final LocalDate now = LocalDate.now();
        final LocalDate beyondOneYearDate = now.plusYears(5).plusDays(1);

        assertThatThrownBy(() ->
            new TaskJpaEntity(
                "제목",
                "내용",
                beyondOneYearDate,
                TODO,
                1L
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(task.getDateErrorMessage());
    }

    @Test
    @DisplayName("할 일 상태를 변경하면, 내용이 반영된다.")
    void whenUpdateTaskThenChangeShouldBeApplied() {
        final String newTitle = "Updated Title";
        final String newContent = "Updated content of the task.";
        final LocalDate today = LocalDate.now();
        final TaskStatus newStatus = TODO;
        final Long newMonthlyTaskId = 300L;
        final TaskColor color = TaskColor.BLUE;

        final TaskJpaEntity newTask = new TaskJpaEntity(
            "Initial Title",
            "Initial content of the task.",
            LocalDate.now(),
            TODO,
            1L
        );

        newTask.update(newTitle, newContent, today, newStatus, newMonthlyTaskId, color);

        assertAll(
            () -> assertEquals(newTitle, newTask.getTitle()),
            () -> assertEquals(newContent, newTask.getContent()),
            () -> assertEquals(today, newTask.getDate()),
            () -> assertEquals(today.getYear(), newTask.getYear()),
            () -> assertEquals(today.getMonthValue(), newTask.getMonth()),
            () -> assertEquals(newStatus, newTask.getStatus()),
            () -> assertEquals(newMonthlyTaskId, newTask.getMonthlyTaskId()),
            () -> assertEquals(color, newTask.getColor())
        );
    }

    @Test
    @DisplayName("monthlyTaskId 값으로 결과를 판단한다.")
    void whenSameMonthlyTaskIdThenResultShouldBeTrue() {
        final LocalDateTime now = LocalDateTime.now();
        final Long monthlyTaskId = (long) now.getMonthValue();
        final LocalDate localDate = now.toLocalDate();
        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            monthlyTaskId,
            1L,
            null,
            now,
            1L,
            now,
            1L,
            false
        );

        assertTrue(newTask.isSameMonth(localDate));
    }

    @Test
    @DisplayName("할 일이 삭제되었다면, deleted 칼럼이 true가 된다.")
    void whenDeleteTaskThenDeleteStatusShouldBeTrue() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            now,
            1L,
            now,
            1L,
            false
        );

        newTask.delete();

        assertTrue(newTask.getDeleted());
    }

    @Test
    @DisplayName("Task와 동일한 달인 경우, True를 반환한다.")
    void whenSameMonthTaskThenResultShouldBeTrue() {
        final LocalDate now = LocalDate.of(2024, 10, 5);
        final TaskJpaEntity newTask = new TaskJpaEntity(
            "프로젝트 관리",
            "프로젝트 진행 상황 점검",
            now.plusDays(10),
            TODO,
            1L
        );

        assertTrue(newTask.isSameMonth(now));
    }

    @Test
    @DisplayName("Task와 다른 달일 경우, False를 반환한다.")
    void whenDifferentMonthTaskThenResultShouldBeFalse() {
        final LocalDate now = LocalDate.of(2024, 10, 5);
        final LocalDate differentMonth = LocalDate.of(2024, 11, 5);
        final TaskJpaEntity newTask = new TaskJpaEntity(
            "프로젝트 관리",
            "프로젝트 진행 상황 점검",
            now,
            TODO,
            1L
        );

        assertFalse(newTask.isSameMonth(differentMonth));
    }

    @Test
    @DisplayName("ID가 같다면 같은 객체로 여긴다.")
    void whenIdIsSameThenInstanceAreSameObj() {
        final TaskJpaEntity expectedTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            LocalDateTime.now(),
            1L,
            LocalDateTime.now(),
            1L,
            false
        );

        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            LocalDateTime.now(),
            1L,
            LocalDateTime.now(),
            1L,
            false
        );

        assertThat(newTask).isEqualTo(expectedTask);
    }

    @Test
    @DisplayName("ID가 다르면 다른 객체로 여긴다.")
    void whenIdIsDifferentThenInstancesAreDifferent() {
        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            LocalDateTime.now(),
            1L,
            LocalDateTime.now(),
            1L,
            false
        );

        final TaskJpaEntity differentTask = new TaskJpaEntity(
            2L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            LocalDateTime.now(),
            1L,
            LocalDateTime.now(),
            1L,
            false
        );

        assertThat(newTask).isNotEqualTo(differentTask);
    }

    @Test
    @DisplayName("ID가 같다면 해시코드가 동일하다.")
    void whenIdIsSameThenHashcodeIsSame() {
        final TaskJpaEntity newTask = new TaskJpaEntity(
            1L,
            "제목",
            "내용",
            LocalDate.now().plusDays(10),
            TODO,
            1L,
            1L,
            null,
            LocalDateTime.now(),
            1L,
            LocalDateTime.now(),
            1L,
            false
        );
        final int expected = newTask.hashCode();
        final int hashcode = newTask.hashCode();

        assertThat(expected).isEqualTo(hashcode);
    }
}
