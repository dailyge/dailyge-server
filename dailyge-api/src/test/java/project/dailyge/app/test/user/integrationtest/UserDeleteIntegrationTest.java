package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.IntegrationTestBase;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.domain.user.UserJpaEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import static project.dailyge.domain.user.UserJpaEntity.getUserAlreadyDeletedMessage;

@DisplayName("[IntegrationTest] 사용자 삭제 통합 테스트")
public class UserDeleteIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("존재하는 사용자를 삭제하면, deleted true로 논리삭제 된다.")
    void whenDeleteExistUserThenUserShouldDeletedBeTrue() {
        UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        userWriteUseCase.delete(saveUser.getId());
        UserJpaEntity findUser = userReadUseCase.findById(saveUser.getId());

        assertTrue(findUser.getDeleted());
    }

    @Test
    @DisplayName("이미 삭제된 사용자를 삭제하면, IllegalArgumentException이 발생한다.")
    void whenDeleteExistUserThenIllegalArgumentExceptionShouldBeHappen() {
        UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        userWriteUseCase.delete(saveUser.getId());

        assertThatThrownBy(() -> userWriteUseCase.delete(saveUser.getId()))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(getUserAlreadyDeletedMessage());
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제하면, UserNotFoundException이 발생한다.")
    void whenDeleteNotExistUserThenUserNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userWriteUseCase.delete(999L))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .hasMessage(USER_NOT_FOUND.message());
    }
}
