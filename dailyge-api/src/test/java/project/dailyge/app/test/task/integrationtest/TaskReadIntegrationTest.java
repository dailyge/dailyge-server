package project.dailyge.app.test.task.integrationtest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;

@DisplayName("[IntegrationTest] 할 일 조회 통합 테스트")
class TaskReadIntegrationTest extends DatabaseTestBase {

    private MonthlyTaskJpaEntity monthlyTask;
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
        monthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);
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
        final MonthlyTaskJpaEntity findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);

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
}
