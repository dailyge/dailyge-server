package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.entity.user.UserJpaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.DUPLICATED_EMAIL;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_SERVICE_UNAVAILABLE;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;

@DisplayName("[IntegrationTest] 사용자 저장 통합 테스트")
class UserCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserFacade userFacade;

    @Test
    @DisplayName("사용자 등록이 정상적으로 성공하면, 사용자 ID는 NULL이 아니다.")
    void whenUserSaveSuccessThenUserIdShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteService.save(createUser(null, "dailyges", "dailyges@gmail.com"));

        assertNotNull(saveUser.getId());
    }

    @Test
    @DisplayName("사용자 등록 시 삭제되지 않은 동일한 이메일이 있을 경우, DuplicatedEmailException이 발생한다.")
    void whenUserEmailDuplicatedThenDuplicatedEmailExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userWriteService.save(newUser))
            .isExactlyInstanceOf(UserTypeException.from(DUPLICATED_EMAIL).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(DUPLICATED_EMAIL.message());
    }

    @Test
    @DisplayName("최초 로그인 시, 사용자가 회원가입된다.")
    void whenFirstLoginThenUserIsShouldBeRegister() {
        final String name = "dailyges";
        final String email = "dailyges@gmail.com";
        userFacade.login(name);

        assertNotNull(userReadService.findUserIdByEmail(email));
    }

    @Test
    @DisplayName("로그인 시 블랙리스트일 경우, UserServiceUnAvailableException이 발생한다.")
    void whenBlacklistUserLoginThenUserServiceUnAvailableExceptionShouldBeHappen() {
        final String name = "blacklistUser";
        final String email = "blacklistUser@gmail.com";
        final UserJpaEntity blacklistUser = new UserJpaEntity(null, name, email, true);
        userWriteService.save(blacklistUser);

        assertThatThrownBy(() -> userFacade.login(name))
            .isExactlyInstanceOf(UserTypeException.from(USER_SERVICE_UNAVAILABLE).getClass())
            .hasMessage(USER_SERVICE_UNAVAILABLE.message());
    }

    @Test
    @DisplayName("정상 사용자가 로그인 시, 예외가 발생하지 않는다.")
    void whenLoginNormalUserThenShouldNotThrow() {
        final String name = "dailyges";
        final String email = "dailyges@gmail.com";
        final UserJpaEntity registeredUser = new UserJpaEntity(null, name, email, false);
        userWriteService.save(registeredUser);

        assertDoesNotThrow(() -> userFacade.login(name));
    }
}
