package project.dailyge.app.test.task.integrationtest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;

import java.time.LocalDate;
import java.util.UUID;

@DisplayName("[IntegrationTest] 할 일 조회 통합 테스트")
public class TaskReadIntegrationTest extends DatabaseTestBase {

    private MonthlyTaskDocument monthlyTask;
    private TaskCreateCommand taskCreateCommand;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskDocumentReadRepository taskReadRepository;

    @BeforeEach
    void setUp() {
        newUser = userWriteUseCase.save(createUserJpaEntity());
        dailygeUser = new DailygeUser(newUser);
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        monthlyTask = taskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getUserId(), now).get();
        taskCreateCommand = createTaskCreationCommand(monthlyTask.getId());
    }

    @Test
    @DisplayName("MonthlyTask가 존재하면, Id와 Date로 조회할 수 있다.")
    void whenMonthlyTaskExistsThenResultShouldNotBeNull() {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);

        assertNotNull(findMonthlyTask);
    }

    @Test
    @DisplayName("MonthlyTask가 존재하면, Id를 조회할 수 있다.")
    void whenMonthlyTaskExistsThenIdShouldNotBeNull() {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);

        assertNotNull(taskReadUseCase.findMonthlyTaskById(dailygeUser, findMonthlyTask.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 MonthlyTaskId를 조회하면 TaskTypeException이 발생한다.")
    void whenMonthlyTaskIdNotExistsThenThrowTaskTypeException() {
        assertThatThrownBy(() -> taskReadUseCase.findMonthlyTaskById(dailygeUser, UUID.randomUUID().toString()))
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
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateCommand);
        final TaskDocument findTaskDocument = taskReadRepository.findTaskDocumentByIdsAndDate(
            dailygeUser.getId(), newTaskId, now
        ).orElseThrow();

        assertNotNull(findTaskDocument);
    }

    @Test
    @DisplayName("존재하지 않는 Task를 조회하면 TaskTypeException이 발생한다.")
    void whenTaskNotExistsThenThrowTaskTypeException() {
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateCommand);

        assertThatThrownBy(() -> taskReadUseCase.findByIdAndDate(
            invalidUser, newTaskId, now
        )).isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_NOT_FOUND.message());
    }
}
