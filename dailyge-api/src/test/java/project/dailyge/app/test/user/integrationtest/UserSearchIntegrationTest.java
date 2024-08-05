package project.dailyge.app.test.user.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.UnAuthorizedException;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_FOUND_MESSAGE;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import project.dailyge.app.core.user.exception.UserTypeException;
import static project.dailyge.app.test.user.fixture.UserFixture.EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@DisplayName("[IntegrationTest] 사용자 조회 통합 테스트")
class UserSearchIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("존재하는 사용자를 ID로 조회한다면, 사용자 정보는 Null 이 아니다.")
    void whenFindExistingUserByIdThenUserShouldBeNotNull() {
        final UserJpaEntity user = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final UserJpaEntity findUser = userReadUseCase.findById(user.getId());

        assertNotNull(findUser);
    }

    @Test
    @DisplayName("사용자가 없다면, UserNotFoundException이 발생한다.")
    void whenFindNonExistentUserThenUserNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("활동중인 사용자를 ID로 조회한다면, 사용자 정보는 Null 이 아니다.")
    void whenFindActiveUserByIdThenUserShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(saveUser.getId());

        assertNotNull(findUser);
    }

    @Test
    @DisplayName("활동중인 사용자가 없다면, UserActiveNotFoundException이 발생한다.")
    void whenFindNonActiveUserThenUserActiveNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findActiveUserById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("로그인 된 사용자 조회 시 있다면, 조회에 성공한다.")
    void whenFindLoggedUserExistsThenUserShouldBeNotNull() {
        final UserJpaEntity loginUser = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final UserJpaEntity findUser = userReadUseCase.findAuthorizedUserById(loginUser.getId());

        assertNotNull(findUser);
        assertEquals(loginUser, findUser);
    }

    @Test
    @DisplayName("로그인 된 사용자 조회 시 없다면, UnAuthorizedException이 발생한다.")
    void whenFindLoggedUserNonExistentThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findAuthorizedUserById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("등록된 이메일로 사용자를 조회 시, Optional 값이 존재한다.")
    void whenFindUserByRegisteredEmailThenResultShouldBeTrue() {
        final UserJpaEntity user = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(user.getEmail());

        assertTrue(findUser.isPresent());
    }

    @Test
    @DisplayName("등록되지 않은 이메일로 사용자를 조회 시, Optional 값이 존재하지 않는다.")
    void whenFindUserByUnregisteredEmailThenResultShouldBeFalse() {
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail("notExist@gmail.com");

        assertFalse(findUser.isPresent());
    }

    @DisplayName("동일 이메일로 재 가입한 사용자를 이메일로 조회 시, 삭제 되지 않은 정보만 검색된다.")
    void whenFindUserReRegisteredBySameEmailThenActiveUserShouldBeOne() {
        final UserJpaEntity deleteUser = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        userWriteUseCase.delete(deleteUser.getId());

        final UserJpaEntity activeUser = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(EMAIL);

        assertAll(
            () -> assertTrue(findUser.isPresent()),
            () -> assertEquals(activeUser.getId(), findUser.get().getId()),
            () -> assertNotEquals(deleteUser.getId(), findUser.get().getId())
        );
    }

    @Test
    @DisplayName("사용자가 존재할 경우, true 를 반환한다.")
    void whenUserExistentUserThenResultShouldBeTrue() {
        final UserJpaEntity user = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));

        assertTrue(userReadUseCase.existsById(user.getId()));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 경우, false 를 반환한다.")
    void whenUserNonExistentThenResultShouldBeTrue() {
        assertFalse(userReadUseCase.existsById(Long.MAX_VALUE));
    }
}
