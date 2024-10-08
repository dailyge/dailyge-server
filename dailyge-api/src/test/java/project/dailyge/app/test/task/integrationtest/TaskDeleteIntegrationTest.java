package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[IntegrationTest] Task 삭제 통합 테스트")
class TaskDeleteIntegrationTest extends DatabaseTestBase {

    private TaskCreateCommand taskCreateCommand;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskWriteService taskWriteService;

    @BeforeEach
    void setUp() {
        now = now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        taskCreateCommand = createTaskCreationCommand(now);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID가 입력되면, UnAuthorizedException이 발생한다.")
    void whenTaskDoesNotExistsThenUnAuthorizedExceptionShouldBeHappen() {
        final Long newTaskId = taskWriteService.save(dailygeUser, taskCreateCommand);
        final DailygeUser invalidDailygeUser = new DailygeUser(Long.MAX_VALUE, NORMAL);

        assertThatThrownBy(() -> taskWriteService.delete(invalidDailygeUser, newTaskId))
            .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("존재하는 Task이 삭제되면, 예외가 발생하지 않는다.")
    void whenDeleteExistenceTaskThenTaskTypeExceptionShouldNotBeHappen() {
        final Long newTaskId = taskWriteService.save(dailygeUser, taskCreateCommand);
        assertDoesNotThrow(() -> taskWriteService.delete(dailygeUser, newTaskId));
    }

    @Test
    @DisplayName("존재하지 않는 Task을 삭제하면, TaskTypeException이 발생한다.")
    void whenDeleteNotExistenceTaskThenTaskTypeExceptionShouldBeHappen() {
        final Long invalidTaskId = Long.MAX_VALUE;

        assertThatThrownBy(() -> taskWriteService.delete(dailygeUser, invalidTaskId))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_NOT_FOUND.message());
    }
}
