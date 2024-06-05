package project.dailyge.domain.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.domain.task.TaskJpaEntity;
import static project.dailyge.domain.task.TaskJpaEntity.getBeyondOneYearErrorMessage;
import static project.dailyge.domain.task.TaskJpaEntity.getOverMaxContentLengthErrorMessage;
import static project.dailyge.domain.task.TaskJpaEntity.getOverMaxTitleLengthErrorMessage;
import static project.dailyge.domain.task.TaskJpaEntity.getPastDateErrorMessage;
import project.dailyge.domain.task.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DisplayName("[UnitTest] 할 일 엔티티 테스트")
class TaskJpaEntityTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 할 일이 생성된다.")
    void taskCreateTest() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskJpaEntity task = TaskJpaEntity.builder()
            .id(1L)
            .title("프로젝트 관리")
            .content("프로젝트 진행 상황 점검")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(now)
            .createdBy(1L)
            .lastModifiedAt(now)
            .lastModifiedBy(1L)
            .deleted(false)
            .build();

        assertAll(
            () -> assertThat(task.getId()).isEqualTo(1L),
            () -> assertThat(task.getTitle()).isEqualTo("프로젝트 관리"),
            () -> assertThat(task.getContent()).isEqualTo("프로젝트 진행 상황 점검"),
            () -> assertThat(task.getDate()).isEqualTo(LocalDate.now().plusDays(10)),
            () -> assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO),
            () -> assertThat(task.getUserId()).isEqualTo(1L),
            () -> assertThat(task.getCreatedAt()).isEqualTo(now),
            () -> assertThat(task.getCreatedBy()).isEqualTo(1L),
            () -> assertThat(task.getLastModifiedAt()).isEqualTo(now),
            () -> assertThat(task.getLastModifiedBy()).isEqualTo(1L),
            () -> assertThat(task.getDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("올바른 일정이 생성되면 예외가 발생하지 않는다.")
    void whenValidTaskThenShouldNotThrow() {
        final LocalDate futureDate = LocalDate.now().plusMonths(6);
        assertThatCode(() -> TaskJpaEntity.builder()
            .title("유효한 제목")
            .content("유효한 내용")
            .date(futureDate)
            .status(TaskStatus.TODO)
            .userId(1L)
            .build())
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입력 가능한 최대 제목 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenTitleIsTooLongThenIllegalArgumentExceptionShouldBeOccur() {
        final String longTitle = "t".repeat(151);
        final LocalDate futureDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() ->
            TaskJpaEntity.builder()
                .title(longTitle)
                .content("내용")
                .date(futureDate)
                .status(TaskStatus.TODO)
                .userId(1L)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(getOverMaxTitleLengthErrorMessage());
    }

    @Test
    @DisplayName("입력 가능한 내용의 최대 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenContentIsTooLongThenIllegalArgumentExceptionShouldBeOccur() {
        final String longContent = "c".repeat(2501);
        final LocalDate futureDate = LocalDate.now().plusDays(10);

        assertThatThrownBy(() ->
            TaskJpaEntity.builder()
                .title("제목")
                .content(longContent)
                .date(futureDate)
                .status(TaskStatus.TODO)
                .userId(1L)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(getOverMaxContentLengthErrorMessage());
    }

    @Test
    @DisplayName("과거 날짜 사용 시 IllegalArgumentException가 발생한다.")
    void whenDateIsBeforeThenIllegalArgumentExceptionShouldBeOccur() {
        final LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() ->
            TaskJpaEntity.builder()
                .title("제목")
                .content("내용")
                .date(pastDate)
                .status(TaskStatus.TODO)
                .userId(1L)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(getPastDateErrorMessage());
    }

    @Test
    @DisplayName("1년 초과 날짜 사용 시 IllegalArgumentException가 발생한다.")
    void whenDateIsTooFarThenIllegalArgumentExceptionShouldBeOccur() {
        final LocalDate beyondOneYearDate = LocalDate.now().plusYears(1).plusDays(1);

        assertThatThrownBy(() ->
            TaskJpaEntity.builder()
                .title("제목")
                .content("내용")
                .date(beyondOneYearDate)
                .status(TaskStatus.TODO)
                .userId(1L)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(getBeyondOneYearErrorMessage());
    }

    @Test
    @DisplayName("할 일이 삭제되었다면, deleted 칼럼이 true가 된다.")
    void whenDeleteTaskThenDeleteStatusShouldBeTrue() {
        final LocalDateTime now = LocalDateTime.now();
        final TaskJpaEntity task = TaskJpaEntity.builder()
            .id(1L)
            .title("프로젝트 관리")
            .content("프로젝트 진행 상황 점검")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(now)
            .createdBy(1L)
            .lastModifiedAt(now)
            .lastModifiedBy(1L)
            .deleted(false)
            .build();

        task.delete();

        assertTrue(task.getDeleted());
    }

    @Test
    @DisplayName("ID가 같다면 같은 객체로 여긴다.")
    void whenIdIsSameThenInstanceAreSameObj() {
        final TaskJpaEntity task = TaskJpaEntity.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(LocalDateTime.now())
            .createdBy(1L)
            .lastModifiedAt(LocalDateTime.now())
            .lastModifiedBy(1L)
            .deleted(false)
            .build();

        assertThat(task).isEqualTo(task);
    }

    @Test
    @DisplayName("ID가 다르면 다른 객체로 여긴다.")
    void whenIdIsDifferentThenInstancesAreDifferent() {
        final TaskJpaEntity task = TaskJpaEntity.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(LocalDateTime.now())
            .createdBy(1L)
            .lastModifiedAt(LocalDateTime.now())
            .lastModifiedBy(1L)
            .deleted(false)
            .build();

        final TaskJpaEntity differentTask = TaskJpaEntity.builder()
            .id(2L)
            .title("제목")
            .content("내용")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(LocalDateTime.now())
            .createdBy(1L)
            .lastModifiedAt(LocalDateTime.now())
            .lastModifiedBy(1L)
            .deleted(false)
            .build();

        assertThat(task).isNotEqualTo(differentTask);
    }

    @Test
    @DisplayName("ID가 같다면 해시코드가 동일하다.")
    void whenIdIsSameThenHashcodeIsSame() {
        final TaskJpaEntity task = TaskJpaEntity.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .date(LocalDate.now().plusDays(10))
            .status(TaskStatus.TODO)
            .userId(1L)
            .createdAt(LocalDateTime.now())
            .createdBy(1L)
            .lastModifiedAt(LocalDateTime.now())
            .lastModifiedBy(1L)
            .deleted(false)
            .build();
        final int hashCode1 = task.hashCode();
        final int hashCode2 = task.hashCode();
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
}
