package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.IntegrationTestBase;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserCodeAndMessage;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.presentation.UserReadApi;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[IntegrationTest] 사용자 조회 통합 테스트")
public class UserSearchIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자가 있다면 사용자 ID로 조회에 성공한다.")
    void whenFindExistUserThenNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final UserJpaEntity findUser = userReadUseCase.findById(saveUser.getId());

        assertNotNull(findUser);
        assertEquals(saveUser, findUser);
    }
    
    @Test
    @DisplayName("사용자가 없다면, UserNotFoundException이 발생한다.")
    void whenFindEmptyUserThenUserNotFoundException() {
        Assertions.assertThrowsExactly(UserTypeException.from(UserCodeAndMessage.USER_NOT_FOUND).getClass(),
                () -> userReadUseCase.findById(1L));
    }

    @Test
    @DisplayName("로그인 된 사용자 조회 시 있다면, 조회에 성공한다.")
    void whenFindLoggedUserExistThenNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final UserJpaEntity findUser = userReadUseCase.findAuthorizedById(saveUser.getId());

        assertNotNull(findUser);
        assertEquals(saveUser, findUser);
    }
    
    @Test
    @DisplayName("로그인 된 사용자 조회 시 없다면, UnAuthorizedException이 발생한다.")
    void whenFindLoggedUserNotExistThenThrowUnAuthorizedException() {
        Assertions.assertThrowsExactly(UnAuthorizedException.class,
            () -> userReadUseCase.findAuthorizedById(1L));
    }

    @Test
    @DisplayName("등록된 이메일로 사용자를 조회 시, Optional 값이 존재한다.")
    void whenFindUserByExistEmailThenIsPresentTrue() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final Optional<UserJpaEntity> byEmail = userReadUseCase.findByEmail(saveUser.getEmail());

        Assertions.assertTrue(byEmail.isPresent());
    }

    @Test
    @DisplayName("등록되지 않은 이메일로 사용자를 조회 시, Optional 값이 존재하지 않는다.")
    void whenFindUserByNotExistEmailThenIsPresentFalse() {
        final Optional<UserJpaEntity> byEmail = userReadUseCase.findByEmail("notExist@gmail.com");

        Assertions.assertFalse(byEmail.isPresent());
    }
}
