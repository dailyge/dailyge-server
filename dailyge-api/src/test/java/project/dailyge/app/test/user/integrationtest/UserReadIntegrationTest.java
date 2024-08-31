package project.dailyge.app.test.user.integrationtest;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.entity.user.UserJpaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.*;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import static project.dailyge.app.test.user.integrationtest.TokenManagerIntegrationTest.*;

@DisplayName("[IntegrationTest] 사용자 조회 통합 테스트")
class UserReadIntegrationTest extends DatabaseTestBase {

    private static final String NAME = "dailyges";
    private static final String EMAIL = "dailyges@gmail.com";


    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("등록된 사용자를 조회하면, Null이 아니다.")
    void whenFindUserThenUserShouldBeNotNull() {
        final UserJpaEntity user = userWriteUseCase.save(createUser(2L, NAME, EMAIL));
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
        final UserJpaEntity saveUser = userWriteUseCase.save(createUser(2L, NAME, EMAIL));
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
            .isInstanceOf(CommonException.class)
            .isInstanceOf(RuntimeException.class)
            .extracting(DETAIL_MESSAGE)
            .isEqualTo(USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("이메일로 사용자를 조회 시, 값이 존재한다.")
    void whenFindUserByRegisteredEmailThenResultShouldBeTrue() {
        final UserJpaEntity user = userWriteUseCase.save(createUser(2L, NAME, EMAIL));
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail(user.getEmail());

        assertTrue(findUser.isPresent());
    }

    @Test
    @DisplayName("존재하지 않은 이메일로 조회 시, 값이 나오지 않는다.")
    void whenFindUserByUnregisteredEmailThenResultShouldBeFalse() {
        final Optional<UserJpaEntity> findUser = userReadUseCase.findActiveUserByEmail("notExist@gmail.com");

        assertFalse(findUser.isPresent());
    }

    @Test
    @DisplayName("동일한 이메일로 재 가입 시, 삭제 되지 않은 정보만 검색된다.")
    void whenFindUserReRegisteredBySameEmailThenActiveUserShouldBeOne() {
        final UserJpaEntity deleteUser = userWriteUseCase.save(createUser(2L, NAME, EMAIL));
        userWriteUseCase.delete(deleteUser.getId());

        final UserJpaEntity activeUser = userWriteUseCase.save(createUser(3L, NAME, EMAIL));
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
        final UserJpaEntity user = userWriteUseCase.save(createUser(2L, NAME, EMAIL));

        assertTrue(userReadUseCase.existsById(user.getId()));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 경우, false 를 반환한다.")
    void whenUserNonExistentThenResultShouldBeTrue() {
        assertFalse(userReadUseCase.existsById(Long.MAX_VALUE));
    }
}
