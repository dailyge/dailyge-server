package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_FOUND_MESSAGE;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.ACTIVE_USER_NOT_FOUND;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import static project.dailyge.app.fixture.user.UserFixture.EMAIL;

@DisplayName("[IntegrationTest] 사용자 조회 통합 테스트")
public class UserSearchIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("존재하는 사용자를 ID로 조회한다면, 사용자 정보는 Null 이 아니다.")
    void whenFindExistingUserByIdThenUserShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(saveUser.getId());

        assertNotNull(findUser);
    }
    
    @Test
    @DisplayName("사용자가 없다면, UserNotFoundException이 발생한다.")
    void whenFindNonExistentUserThenUserNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findActiveUserById(1L))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(ACTIVE_USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("로그인 된 사용자 조회 시 있다면, 조회에 성공한다.")
    void whenFindLoggedUserExistsThenUserShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final UserJpaEntity findUser = userReadUseCase.findAuthorizedById(saveUser.getId());

        assertNotNull(findUser);
        assertEquals(saveUser, findUser);
    }
    
    @Test
    @DisplayName("로그인 된 사용자 조회 시 없다면, UnAuthorizedException이 발생한다.")
    void whenFindLoggedUserNonExistentThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findAuthorizedById(1L))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("등록된 이메일로 사용자를 조회 시, Optional 값이 존재한다.")
    void whenFindUserByRegisteredEmailThenResultShouldBeTrue() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(saveUser.getEmail());

        assertTrue(findUser.isPresent());
    }

    @Test
    @DisplayName("등록되지 않은 이메일로 사용자를 조회 시, Optional 값이 존재하지 않는다.")
    void whenFindUserByUnregisteredEmailThenResultShouldBeFalse() {
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail("notExist@gmail.com");

        assertFalse(findUser.isPresent());
    }

    @Test
    @DisplayName("동일 이메일로 재 가입한 사용자를 이메일로 조회 시, 삭제 되지 않은 정보만 검색된다.")
    void whenFindUserReRegisteredBySameEmailThenActiveUserShouldBeOne() {
        final UserJpaEntity deleteUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        userWriteUseCase.delete(deleteUser.getId());

        final UserJpaEntity activeUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(EMAIL);

        assertAll(
            () -> assertTrue(findUser.isPresent()),
            () -> assertEquals(activeUser.getId(), findUser.get().getId()),
            () -> assertNotEquals(deleteUser.getId(), findUser.get().getId())
        );

    }
}
