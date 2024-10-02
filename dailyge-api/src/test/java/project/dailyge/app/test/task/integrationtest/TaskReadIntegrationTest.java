package project.dailyge.app.test.task.integrationtest;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;
import static com.mongodb.assertions.Assertions.assertFalse;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;

@DisplayName("[IntegrationTest] 할 일 조회 통합 테스트")
class TaskReadIntegrationTest extends DatabaseTestBase {

    private TaskCreateCommand taskCreateCommand;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        taskCreateCommand = createTaskCreationCommand(now);
    }

    @Test
    @DisplayName("MonthlyTask가 존재하면, Id와 Date로 조회할 수 있다.")
    void whenMonthlyTaskExistsThenResultShouldNotBeNull() {
        final MonthlyTaskJpaEntity findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);

        assertNotNull(findMonthlyTask);
    }

    @Test
    @DisplayName("MonthlyTask가 존재하면, Id를 조회할 수 있다.")
    void whenMonthlyTaskExistsThenIdShouldNotBeNull() {
        assertNotNull(taskReadUseCase.findTasksByMonthlyTasksIdAndDate(dailygeUser, now));
    }

    @Test
    @DisplayName("존재하지 않는 MonthlyTaskId를 조회하면 TaskTypeException이 발생한다.")
    void whenMonthlyTaskIdNotExistsThenThrowTaskTypeException() {
        assertThatThrownBy(() -> taskReadUseCase.findMonthlyTaskById(Long.MAX_VALUE))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(MONTHLY_TASK_NOT_FOUND.message());
    }

    @Test
    @DisplayName("존재하지 않는 MonthlyTask를 조회하면 TaskTypeException이 발생한다.")
    void whenMonthlyTaskNotExistsThenThrowTaskTypeException() {
        assertThatThrownBy(() -> taskReadUseCase.findMonthlyTaskByUserIdAndDate(invalidUser, now))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(MONTHLY_TASK_NOT_FOUND.message());
    }

    @Test
    @DisplayName("Task가 존재하면, Id와 Date로 조회할 수 있다.")
    void whenTaskExistsThenIdShouldNotBeNull() {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateCommand);
        final TaskJpaEntity findTask = taskReadUseCase.findTaskById(dailygeUser, newTaskId);

        assertNotNull(findTask);
    }

    @Test
    @DisplayName("존재하지 않는 Task를 조회하면 TaskTypeException이 발생한다.")
    void whenTaskNotExistsThenThrowTaskTypeException() {
        final Long invalidTaskId = Long.MAX_VALUE;

        assertThatThrownBy(() -> taskReadUseCase.findTaskById(
            dailygeUser, invalidTaskId
        )).isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_NOT_FOUND.message());
    }

    @Test
    @DisplayName("존재하는 유저와 날짜 범위로 주간 할 일을 조회하면 결과가 반환된다.")
    void whenValidUserIdAndDatesProvidedThenReturnTasks() {
        taskWriteUseCase.save(dailygeUser, taskCreateCommand);

        final LocalDate startDate = now;
        final LocalDate endDate = now.plusDays(10);
        final Tasks tasks = taskReadUseCase.findTasksStatisticByUserIdAndDate(dailygeUser, startDate, endDate);

        assertTrue(tasks.size() > 0);
        for (final TaskJpaEntity taskEntity : tasks.getTaskEntities()) {
            assertFalse(startDate.isAfter(taskEntity.getDate()));
            assertFalse(endDate.isBefore(taskEntity.getDate()));
        }
    }

    @Test
    @DisplayName("존재하지 않는 MonthlyTaskIds가 있을 경우 빈 Tasks를 반환한다.")
    void whenNoMonthlyTasksFoundThenReturnEmptyTasks() {
        final LocalDate invalidStartDate = LocalDate.now().minusYears(10);
        final LocalDate invalidEndDate = LocalDate.now().minusYears(10).plusDays(7);

        final Tasks tasks = taskReadUseCase.findTasksStatisticByUserIdAndDate(dailygeUser, invalidStartDate, invalidEndDate);
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("날짜 범위에 맞는 Tasks가 없으면 빈 리스트를 반환한다.")
    void whenNoTasksForDateRangeThenReturnEmptyTasks() {
        final LocalDate futureStartDate = LocalDate.now().plusYears(15);
        final LocalDate futureEndDate = futureStartDate.plusDays(7);

        final Tasks tasks = taskReadUseCase.findTasksStatisticByUserIdAndDate(dailygeUser, futureStartDate, futureEndDate);
        assertEquals(0, tasks.size());
    }
}
