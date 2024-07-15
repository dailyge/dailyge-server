package project.dailyge.app.test.task.unittest;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.task.application.service.TaskValidator;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;
import static project.dailyge.entity.task.TaskStatus.TODO;
import static project.dailyge.entity.user.Role.ADMIN;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[UnitTest] 할 일 검증 단위 테스트")
class TaskValidatorUnitTest extends DatabaseTestBase {

    private TaskValidator validator;

    @Autowired
    private TaskDocumentReadRepository taskReadRepository;

    @BeforeEach
    void setUp() {
        validator = new TaskValidator(taskReadRepository);
    }

    @Test
    @DisplayName("관리자면 권한 예외가 발생하지 않는다.")
    void whenAdminUserThenAuthExceptionShouldNotBeHappen() {
        final DailygeUser dailygeUser = new DailygeUser(1L, ADMIN);
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, 300L);

        assertDoesNotThrow(() -> validator.validateAuth(dailygeUser, newTask));
    }

    @Test
    @DisplayName("일반 사용자일 때, 자신이 작성한 할 일이 아니라면, UnAuthorizedException이 발생한다.")
    void whenNormalUserIsNotTaskOwnerThenAuthExceptionShouldBeHappen() {
        final DailygeUser dailygeUser = new DailygeUser(1L, NORMAL);
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, 300L);

        assertThatThrownBy(() -> validator.validateAuth(dailygeUser, newTask))
            .isInstanceOf(UnAuthorizedException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("일반 사용자일 때, 자신이 작성한 할 일이라면, 예외가 발생하지 않는다.")
    void whenNormalUserIsTaskOwnerThenAuthExceptionShouldNotBeHappen() {
        final DailygeUser dailygeUser = new DailygeUser(1L, NORMAL);
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, dailygeUser.getUserId());

        assertDoesNotThrow(() -> validator.validateAuth(dailygeUser, newTask));
    }
}
