package project.dailyge.app.test.user.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import project.dailyge.app.core.user.exception.UserTypeException;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.entity.user.UserJpaEntity;

@DisplayName("[IntegrationTest] 사용자 삭제 통합 테스트")
class UserDeleteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserWriteService userWriteService;

    @Test
    @DisplayName("존재하는 사용자를 삭제하면, deleted true로 논리삭제 된다.")
    void whenDeleteAnExistingUserThenUserShouldDeletedBeTrue() {
        userWriteService.delete(dailygeUser.getId());
        final UserJpaEntity findUser = userReadService.findById(dailygeUser.getId());

        assertTrue(findUser.getDeleted());
    }

    @Test
    @DisplayName("이미 삭제된 사용자를 삭제하면, IllegalArgumentException이 발생한다.")
    void whenDeleteAlreadyDeletedUserThenIllegalArgumentExceptionShouldBeHappen() {
        final UserJpaEntity saveUser = userWriteService.save(createUser(null, "dailyges", "dailyges@gmail.com"));
        userWriteService.delete(saveUser.getId());

        assertThatThrownBy(() -> userWriteService.delete(saveUser.getId()))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제하면, UserNotFoundException이 발생한다.")
    void whenDeleteNonExistentUserThenUserNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userWriteService.delete(999L))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }
}
