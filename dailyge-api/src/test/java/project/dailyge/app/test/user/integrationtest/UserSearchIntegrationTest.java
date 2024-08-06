package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_FOUND_MESSAGE;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import static project.dailyge.app.test.user.fixture.UserFixture.EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;

@DisplayName("[IntegrationTest] 사용자 조회 통합 테스트")
class UserSearchIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("등록된 사용자를 조회하면, Null이 아니다.")
    void whenFindUserThenUserShouldBeNotNull() {
        final UserJpaEntity user = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final UserJpaEntity findUser = userReadUseCase.findById(user.getId());

        assertNotNull(findUser);
    }

    @Test
    @DisplayName("등록된 사용자가 없다면, UserNotFoundException이 발생한다.")
    void whenFindNonExistentUserThenUserNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("사용자를 조회하면, Null이 아니다.")
    void whenActiveUserFindThenUserShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(saveUser.getId());

        assertNotNull(findUser);
    }

    @Test
    @DisplayName("사용자 조회 시 없다면, UserActiveNotFoundException이 발생한다.")
    void whenFindNonActiveUserThenUserActiveNotFoundExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UserTypeException.from(USER_NOT_FOUND).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("사용자 조회 시 없다면, UnAuthorizedException이 발생한다.")
    void whenFindLoggedUserNonExistentThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userReadUseCase.findAuthorizedUserById(Long.MAX_VALUE))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(USER_NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("이메일로 사용자를 조회 시, 값이 존재한다.")
    void whenFindUserByRegisteredEmailThenResultShouldBeTrue() {
        final UserJpaEntity user = userWriteUseCase.save(createUser("dailyges", "dailyges@gmail.com"));
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(user.getEmail());

        assertTrue(findUser.isPresent());
    }

    @Test
    @DisplayName("미 가입 이메일로 조회 시, 값이 존재하지 않는다.")
    void whenFindUserByUnregisteredEmailThenResultShouldBeFalse() {
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail("notExist@gmail.com");

        assertFalse(findUser.isPresent());
    }

    @DisplayName("동일한 이메일로 재 가입 시, 삭제 되지 않은 정보만 검색된다.")
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
