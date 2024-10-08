package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadService;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskUpdateCommand;
import project.dailyge.entity.task.TaskJpaEntity;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[IntegrationTest] Task 수정 통합 테스트")
class TaskUpdateIntegrationTest extends DatabaseTestBase {

    private TaskCreateCommand taskCreateCommand;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadService taskReadService;

    @Autowired
    private TaskWriteService taskWriteService;

    @BeforeEach
    void setUp() {
        now = now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        taskCreateCommand = createTaskCreationCommand(now);
    }

    @Test
    @DisplayName("Task를 업데이트하면, 내용이 반영된다.")
    void whenTaskUpdateThenContentsShouldBeHappen() {
        final Long newTaskId = taskWriteService.save(dailygeUser, taskCreateCommand);
        final TaskUpdateCommand taskUpdateCommand = createTaskUpdateCommand(now);
        taskWriteService.update(dailygeUser, newTaskId, taskUpdateCommand);

        final TaskJpaEntity findTask = taskReadService.findTaskById(dailygeUser, newTaskId);

        assertAll(
            () -> assertEquals(taskUpdateCommand.title(), findTask.getTitle()),
            () -> assertEquals(taskUpdateCommand.content(), findTask.getContent()),
            () -> assertEquals(taskUpdateCommand.status(), findTask.getStatus()),
            () -> assertEquals(taskUpdateCommand.date(), findTask.getDate()),
            () -> assertEquals(taskUpdateCommand.color(), findTask.getColor())
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID가 입력되면, TaskTypeException이 발생한다.")
    void whenInvalidUserIdThenUnAuthorizedExceptionShouldBeHappen() {
        final Long invalidUserId = 100_000L;
        final Long newTaskId = taskWriteService.save(dailygeUser, taskCreateCommand);
        final DailygeUser dailygeUser = new DailygeUser(invalidUserId, NORMAL);

        final TaskUpdateCommand taskUpdateCommand = createTaskUpdateCommand(now);
        assertThatThrownBy(() -> taskWriteService.update(dailygeUser, newTaskId, taskUpdateCommand))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(MONTHLY_TASK_NOT_FOUND.message());
    }
}
