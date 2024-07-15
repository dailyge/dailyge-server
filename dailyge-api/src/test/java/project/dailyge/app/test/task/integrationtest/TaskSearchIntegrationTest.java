//package project.dailyge.app.test.task.integrationtest;
//
//
//import static java.time.LocalDate.now;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import project.dailyge.app.common.DatabaseTestBase;
//import project.dailyge.app.common.auth.DailygeUser;
//import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
//import project.dailyge.app.common.exception.UnAuthorizedException;
//import project.dailyge.app.core.task.application.TaskReadUseCase;
//import project.dailyge.app.core.task.exception.TaskCodeAndMessage;
//import project.dailyge.app.core.task.exception.TaskTypeException;
//import project.dailyge.app.core.task.facade.TaskFacade;
//import project.dailyge.app.core.user.application.UserWriteUseCase;
//import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
//import project.dailyge.entity.task.TaskJpaEntity;
//import static project.dailyge.entity.task.TaskStatus.TODO;
//import static project.dailyge.entity.user.Role.NORMAL;
//import project.dailyge.entity.user.UserJpaEntity;
//
//@DisplayName("[IntegrationTest] 할 일 조회 통합 테스트")
//public class TaskSearchIntegrationTest extends DatabaseTestBase {
//
//    @Autowired
//    private TaskFacade taskFacade;
//
//    @Autowired
//    private TaskReadUseCase taskReadUseCase;
//
//    @Autowired
//    private UserWriteUseCase userWriteUseCase;
//
//    @Test
//    @DisplayName("할 일이 존재하면, Id로 조회할 수 있다.")
//    void whenTaskExistsThenIdShouldNotBeNull() {
//        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
//        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());
//        final DailygeUser dailygeUser = new DailygeUser(newUser);
//
//        final TaskJpaEntity savedTask = taskFacade.save(newTask);
//
//        final TaskJpaEntity findTask = taskReadUseCase.findById(dailygeUser, savedTask.getId());
//
//        assertNotNull(findTask);
//    }
//
//    @Test
//    @DisplayName("권한이 없는 사용자가 다른 사람의 할 일을 조회하면, UnAuthorizedException이 발생한다.")
//    void whenUserIsUnAuthorizedThenIdShouldNotBeNull() {
//        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
//        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());
//        final Long unAuthorizedUserId = Long.MAX_VALUE;
//        final DailygeUser dailygeUser = new DailygeUser(unAuthorizedUserId, NORMAL);
//
//        final TaskJpaEntity savedTask = taskFacade.save(newTask);
//
//        assertThatThrownBy(() -> taskReadUseCase.findById(dailygeUser, savedTask.getId()))
//            .isInstanceOf(UnAuthorizedException.class)
//            .hasMessage(UN_AUTHORIZED.message());
//    }
//
//    @Test
//    @DisplayName("할 일이 존재하지 않으면, TaskTypeException이 발생한다.")
//    void whenTaskNotExistsThenIdShouldNotBeNull() {
//        final Long invalidTaskId = 300_000_000L;
//        final DailygeUser invalidDailygeUser = new DailygeUser(invalidTaskId, NORMAL);
//        userWriteUseCase.save(createUserJpaEntity());
//
//        assertThatThrownBy(() -> taskReadUseCase.findById(invalidDailygeUser, invalidTaskId))
//            .isInstanceOf(TaskTypeException.class)
//            .hasMessage(TaskCodeAndMessage.TASK_NOT_FOUND.getMessage());
//    }
//}
