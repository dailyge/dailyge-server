package project.dailyge.app.test.common;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.NativeWebRequest;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.configuration.web.AuthArgumentResolver;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static project.dailyge.app.common.exception.JWTAuthenticationException.EMPTY_TOKEN_ERROR_MESSAGE;
import static project.dailyge.app.test.user.fixture.UserFixture.createUserJpaEntity;

@DisplayName("[UnitTest] AuthArgumentResolver 검증 단위 테스트")
class AuthArgumentResolverTest {

    private TokenProvider tokenProvider;
    private AuthArgumentResolver resolver;
    private UserReadUseCase userReadUseCase;
    private NativeWebRequest webRequest;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(
            "secretKey",
            "payloadSecretKey",
            "salt",
            1,
            2
        );
        tokenProvider = new TokenProvider(jwtProperties);
        userReadUseCase = mock(UserReadUseCase.class);
        resolver = new AuthArgumentResolver(userReadUseCase, tokenProvider);
        request = mock(HttpServletRequest.class);
        webRequest = mock(NativeWebRequest.class);
        when(webRequest.getNativeRequest())
            .thenReturn(request);
    }

    @Test
    @DisplayName("토큰정보가 존재하고, 올바르다면 인증 객체가 생성된다.")
    void shouldBeNotNullWhenUserIdIsValid() {
        final UserJpaEntity user = createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(token.getAuthorizationToken());
        when(userReadUseCase.findAuthorizedUserById(user.getId()))
            .thenReturn(user);

        final DailygeUser result = (DailygeUser) resolver.resolveArgument(null, null, webRequest, null);

        assertNotNull(result);
    }

    @Test
    @DisplayName("토큰이 없다면, UnAuthorizedException이 발생한다.")
    void whenTokenIsEmptyThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(EMPTY_TOKEN_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("사용자 ID가 유효하면 예외가 발생하지 않는다.")
    void shouldNotThrowExceptionWhenUserIdIsValid() {
        final Long validUserId = 456L;
        final UserJpaEntity expectedUser = createUserJpaEntity(validUserId);
        final DailygeToken token = tokenProvider.createToken(expectedUser);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(token.getAuthorizationToken());
        when(userReadUseCase.findAuthorizedUserById(validUserId))
            .thenReturn(expectedUser);

        assertDoesNotThrow(
            () -> ((DailygeUser) resolver.resolveArgument(null, null, webRequest, null))
        );
    }
}
