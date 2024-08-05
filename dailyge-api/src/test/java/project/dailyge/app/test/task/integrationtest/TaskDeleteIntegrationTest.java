package project.dailyge.app.test.task.integrationtest;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.user.application.UserWriteUseCase;

@DisplayName("[IntegrationTest] 할 일 삭제 통합 테스트")
public class TaskDeleteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TaskFacade taskFacade;

//    @Test
//    @DisplayName("존재하지 않는 사용자 ID가 입력되면, UserTypeException이 발생한다.")
//    void whenTaskRegisterDoesNotExistsThenUserTypeExceptionShouldBeHappen() {
//        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
//        final DailygeUser dailygeUser = new DailygeUser(newUser);
//        taskFacade.createMonthlyTasks(dailygeUser, LocalDate.now());
//        final TaskCreateCommand command = new TaskCreateCommand(
//            "독서",
//            "Kafka 완벽가이드 1~30p 읽기",
//            now()
//        );
//
//        taskFacade.save(dailygeUser, command);
//        final Long invalidUserId = Long.MAX_VALUE;
//        final DailygeUser invalidDailygeUser = new DailygeUser(invalidUserId, NORMAL);
//
////        assertThatThrownBy(() -> taskFacade.delete(invalidDailygeUser, savedTask.getId()))
////            .isInstanceOf(UserTypeException.class)
////            .hasMessage(ACTIVE_USER_NOT_FOUND.message());
//    }

//    @Test
//    @DisplayName("존재하는 할 일이 삭제되면, 예외가 발생하지 않는다.")
//    void whenDeleteExistenceTaskThenTaskTypeExceptionShouldNotBeHappen() {
//        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
//        final DailygeUser dailygeUser = new DailygeUser(newUser);
//        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());
//
////        final TaskJpaEntity savedTask = taskFacade.save(newTask);
////
////        assertDoesNotThrow(() -> taskFacade.delete(dailygeUser, savedTask.getId()));
//    }

//    @Test
//    @DisplayName("존재하지 않는 할 일을 삭제하면, TaskTypeException이 발생한다.")
//    void whenDeleteNotExistenceTaskThenTaskTypeExceptionShouldBeHappen() {
//        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
//        final DailygeUser dailygeUser = new DailygeUser(newUser);
//        final Long invalidTaskId = Long.MAX_VALUE;
//
//        assertThatThrownBy(() -> taskFacade.delete(dailygeUser, invalidTaskId))
//            .isInstanceOf(TaskTypeException.class)
//            .hasMessage(TASK_NOT_FOUND.message());
//    }
}
