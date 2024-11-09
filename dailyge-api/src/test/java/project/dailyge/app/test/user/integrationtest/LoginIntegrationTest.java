package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.core.cache.user.UserCacheWriteService;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_SERVICE_UNAVAILABLE;
import static project.dailyge.app.test.user.fixture.UserFixture.EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.NICKNAME;

@DisplayName("[IntegrationTest] Login 통합 테스트")
class LoginIntegrationTest extends DatabaseTestBase {

    private static final String CODE = "AuthorizationCode";

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private EventPublisher<UserEvent> userEventPublisher;

    @Autowired
    private EventPublisher<TaskEvent> taskEventEventPublisher;

    @Autowired
    private UserCacheReadService userCacheReadService;

    @Autowired
    private UserCacheWriteService userCacheWriteService;

    private GoogleOAuthManager mockGoogleOAuthManager;

    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        mockGoogleOAuthManager = mock(GoogleOAuthManager.class);

        userFacade = new UserFacade(
            mockGoogleOAuthManager,
            userReadService,
            userWriteService,
            tokenProvider,
            tokenManager,
            userEventPublisher,
            taskEventEventPublisher,
            userCacheReadService,
            userCacheWriteService
        );
    }

    @Test
    @DisplayName("블랙리스트인 사용자가 로그인 하면, UserServiceUnAvailableException이 발생한다.")
    void whenBlacklistUserLoginThenUserServiceUnAvailableExceptionShouldBeThrown() {
        final String code = "AuthorizationCode";
        final String name = "dailyges";
        final String email = "dailyges@gmail.com";
        final UserJpaEntity newUser = new UserJpaEntity(null, name, email, true);
        userWriteService.save(newUser);
        final GoogleUserInfoResponse response = new GoogleUserInfoResponse(name, name, email, "", true);
        when(mockGoogleOAuthManager.getUserInfo(code)).thenReturn(response);

        assertThatThrownBy(() -> userFacade.login(code))
            .isExactlyInstanceOf(UserTypeException.from(USER_SERVICE_UNAVAILABLE).getClass())
            .hasMessage(USER_SERVICE_UNAVAILABLE.message());
    }

    @Test
    @DisplayName("정상 사용자가 로그인 하면, 예외가 발생하지 않는다.")
    void whenUserLoginThenShouldNotThrow() {
        final GoogleUserInfoResponse response = new GoogleUserInfoResponse(NICKNAME, NICKNAME, EMAIL, "", true);
        when(mockGoogleOAuthManager.getUserInfo(CODE)).thenReturn(response);

        assertDoesNotThrow(() -> userFacade.login(CODE));
    }
}
