package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.entity.user.UserJpaEntity;

import java.io.IOException;

import static project.dailyge.app.fixture.user.OAuthFixture.getOAuthAccessResponseFixture;

@DisplayName("[IntegrationTest] 사용자 로그인 통합 테스트")
public class LoginIntegrationTest extends DatabaseTestBase {

    private static final String AUTHENTICATION_CODE = "AuthenticationCode";

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Test
    @DisplayName("로그인에 성공하면, DailygeToken을 생성한다.")
    void whenLoginSuccessThenDailygeTokenIsGenerated() throws IOException {
        DailygeToken dailygeToken = userFacade.login(AUTHENTICATION_CODE);
        Long userId = tokenProvider.getUserId(dailygeToken.accessToken());
        UserJpaEntity findUser = userReadUseCase.findActiveUserById(userId);

        Assertions.assertEquals(getOAuthAccessResponseFixture().get("email"), findUser.getEmail());
    }
}
