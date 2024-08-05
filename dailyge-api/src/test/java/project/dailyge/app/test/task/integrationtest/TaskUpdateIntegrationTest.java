package project.dailyge.app.test.task.integrationtest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskUpdateCommand;
import static project.dailyge.app.test.user.fixture.UserFixture.createAdminUser;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@DisplayName("[IntegrationTest] 할 일 수정 통합 테스트")
class TaskUpdateIntegrationTest extends DatabaseTestBase {

    private MonthlyTaskDocument findMonthlyTask;
    private TaskCreateCommand taskCreateCommand;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskDocumentReadRepository taskReadRepository;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        findMonthlyTask = taskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getUserId(), now).get();
        taskCreateCommand = createTaskCreationCommand(findMonthlyTask.getId());
    }

    @Test
    @DisplayName("Task를 업데이트하면, 내용이 반영된다.")
    void whenTaskUpdateThenContentsShouldBeHappen() {
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateCommand);
        final TaskUpdateCommand taskUpdateCommand = createTaskUpdateCommand(findMonthlyTask.getId());
        taskWriteUseCase.update(dailygeUser, newTaskId, taskUpdateCommand);

        final TaskDocument findTaskDocument = taskReadRepository.findTaskDocumentByIds(
            taskUpdateCommand.monthlyTaskId(),
            newTaskId
        ).orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));

        assertAll(
            () -> assertEquals(taskUpdateCommand.monthlyTaskId(), findTaskDocument.getMonthlyTaskId()),
            () -> assertEquals(taskUpdateCommand.title(), findTaskDocument.getTitle()),
            () -> assertEquals(taskUpdateCommand.content(), findTaskDocument.getContent()),
            () -> assertEquals(taskUpdateCommand.getStatus(), findTaskDocument.getStatus()),
            () -> assertEquals(taskUpdateCommand.date(), findTaskDocument.getLocalDate())
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID가 입력되면, TaskTypeException이 발생한다.")
    void whenInvalidUserIdThenUnAuthorizedExceptionShouldBeHappen() {
        final Long invalidUserId = 100_000L;
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateCommand);
        final DailygeUser dailygeUser = new DailygeUser(invalidUserId, NORMAL);

        final TaskUpdateCommand taskUpdateCommand = createTaskUpdateCommand(findMonthlyTask.getId());
        assertThatThrownBy(() -> taskWriteUseCase.update(dailygeUser, newTaskId, taskUpdateCommand))
            .isInstanceOf(TaskTypeException.class);
    }

    @Test
    @DisplayName("관리자가 Task를 수정하면 권한 예외가 발생하지 않는다.")
    void whenAdminUpdateTaskThenExceptionShouldNotBeHappen() {
        final UserJpaEntity newUser = persist(createAdminUser("dailyge", "dailyge2024@gmail.com"));
        final DailygeUser dailygeUser = new DailygeUser(newUser.getId(), newUser.getRole());
        final LocalDate now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);

        final MonthlyTaskDocument findMonthlyTask = taskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getUserId(), now).get();
        taskCreateCommand = createTaskCreationCommand(findMonthlyTask.getId());
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateCommand);

        final TaskUpdateCommand taskUpdateCommand = createTaskUpdateCommand(findMonthlyTask.getId());
        assertDoesNotThrow(() -> taskWriteUseCase.update(dailygeUser, newTaskId, taskUpdateCommand));
    }
}
