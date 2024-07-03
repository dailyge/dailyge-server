package project.dailyge.app.test.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.web.LoginInterceptor;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@DisplayName("[UnitTest] LoginInterceptor 단위 테스트")
class LoginInterceptorUnitTest {

    private static final String SECRET_KEY = "secretKey";

    private LoginInterceptor loginInterceptor;
    private TokenProvider tokenProvider;
    private TokenManager tokenManager;
    private UserReadUseCase userReadUseCase;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, 1, 2);
        tokenProvider = new TokenProvider(jwtProperties);
        userReadUseCase = mock(UserReadUseCase.class);
        tokenManager = mock(TokenManager.class);
        loginInterceptor = new LoginInterceptor(userReadUseCase, tokenProvider, tokenManager);
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("비로그인 사용자가 로그인 호출 시, true 를 반환한다.")
    void whenNonLoginUserCallsToLoginThenResultShouldBeTrue() {
        assertTrue(loginInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("로그인 사용자가 로그인 호출 시, false 를 반환한다.")
    void whenLoginUserCallsToLoginThenResultShouldBeFalse() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);

        when(request.getHeader(AUTHORIZATION))
            .thenReturn(token.getAuthorizationToken());
        when(userReadUseCase.isExistsUserById(user.getId()))
            .thenReturn(true);

        assertFalse(loginInterceptor.preHandle(request, response, null));
    }
}
