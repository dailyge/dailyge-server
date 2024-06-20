package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import project.dailyge.entity.task.TaskJpaEntity;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;
import static project.dailyge.entity.task.TaskStatus.TODO;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserJpaEntity;

@DisplayName("[IntegrationTest] 할 일 수정 통합 테스트")
public class TaskUpdateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("할 일을 업데이트하면, 내용이 반영된다.")
    void whenTaskUpdateThenContentsShouldBeHappen() {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final TaskJpaEntity newTask = new TaskJpaEntity(
            "독서",
            "Kafka 완벽가이드 1~30p 읽기",
            now(),
            TODO,
            newUser.getId()
        );
        final TaskJpaEntity savedTask = taskWriteUseCase.save(newTask);

        final DailygeUser dailygeUser = new DailygeUser(newUser.getId(), NORMAL);
        final TaskUpdateCommand command = new TaskUpdateCommand(
            "미팅",
            "Backend 부서와 10시 미팅",
            now(),
            IN_PROGRESS
        );
        taskWriteUseCase.update(dailygeUser, savedTask.getId(), command);

        final TaskJpaEntity findTask = taskReadUseCase.findById(dailygeUser, savedTask.getId());
        assertAll(
            () -> assertEquals(command.title(), findTask.getTitle()),
            () -> assertEquals(command.content(), findTask.getContent()),
            () -> assertEquals(command.date(), findTask.getDate()),
            () -> assertEquals(command.status(), findTask.getStatus())
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID가 입력되면, UnAuthorizedException이 발생한다.")
    void whenInvalidUserIdThenUnAuthorizedExceptionShouldBeHappen() {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());
        final TaskJpaEntity savedTask = taskWriteUseCase.save(newTask);

        final Long invalidUserId = Long.MAX_VALUE;
        final DailygeUser dailygeUser = new DailygeUser(invalidUserId, NORMAL);
        final TaskUpdateCommand command = new TaskUpdateCommand(
            "미팅",
            "Backend 부서와 10시 미팅",
            now(),
            IN_PROGRESS
        );
        assertThatThrownBy(() -> taskWriteUseCase.update(dailygeUser, savedTask.getId(), command))
            .isInstanceOf(UnAuthorizedException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("존재하지 않는 할 일 ID가 입력되면, TaskTypeException이 발생한다.")
    void whenInvalidTaskIdThenTaskTypeExceptionShouldBeHappen() {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());
        taskWriteUseCase.save(newTask);

        final Long invalidTaskId = Long.MAX_VALUE;
        final DailygeUser dailygeUser = new DailygeUser(newUser.getId(), NORMAL);
        final TaskUpdateCommand command = new TaskUpdateCommand(
            "미팅",
            "Backend 부서와 10시 미팅",
            now(),
            IN_PROGRESS
        );
        assertThatThrownBy(() -> taskWriteUseCase.update(dailygeUser, invalidTaskId, command))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_NOT_FOUND.message());
    }
}
