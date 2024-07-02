package project.dailyge.app.test.user.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.DUPLICATED_EMAIL;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;

import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.entity.user.UserJpaEntity;

@DisplayName("[IntegrationTest] 사용자 저장 통합 테스트")
class UserRegisterIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자 등록이 정상적으로 성공하면, 사용자 ID는 NULL이 아니다.")
    void whenUserSaveSuccessThenUserIdShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteUseCase.save(createUserJpaEntity());

        Assertions.assertNotNull(saveUser.getId());
    }

    @Test
    @DisplayName("사용자 등록 시 삭제되지 않은 동일한 이메일이 있을 경우, DuplicatedEmailException이 발생한다.")
    void whenUserEmailDuplicatedThenDuplicatedEmailExceptionShouldBeHappen() {
        UserJpaEntity user = userWriteUseCase.save(createUserJpaEntity());

        assertThatThrownBy(() -> userWriteUseCase.save(user))
            .isExactlyInstanceOf(UserTypeException.from(DUPLICATED_EMAIL).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(DUPLICATED_EMAIL.message());
    }
}
