package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.dailyge.app.common.IntegrationTestBase;
import project.dailyge.app.core.user.application.facade.UserFacade;
import project.dailyge.app.core.user.dto.request.UserRegisterRequest;
import project.dailyge.app.core.user.exception.UserCodeAndMessage;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserJpaEntity;

@SpringBootTest
@DisplayName("[IntegrationTest] 사용자 저장 통합 테스트")
public class UserRegisterIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserFacade userFacade;

    @Test
    @DisplayName("사용자 등록이 정상적으로 성공하면, 사용자 ID는 NULL이 아니다.")
    void whenUserSaveSuccessThenUserIdNotNull() {
        final UserRegisterRequest request = new UserRegisterRequest("testName", "test@gmail.com", null);
        final UserJpaEntity save = userFacade.save(request.toEntity());

        Assertions.assertNotNull(save.getId());
    }
    
    @Test
    @DisplayName("동일한 이메일이 있을 경우, UserEmailConflictException이 발생한다. ")
    void whenEmailConflictThenUserEmailConflictException() {
        final UserRegisterRequest request = new UserRegisterRequest("testName", "test@gmail.com", null);
        userFacade.save(request.toEntity());

        Assertions.assertThrowsExactly(UserTypeException.from(UserCodeAndMessage.USER_EMAIL_CONFLICT).getClass(), () -> userFacade.save(request.toEntity()));
    }
}
