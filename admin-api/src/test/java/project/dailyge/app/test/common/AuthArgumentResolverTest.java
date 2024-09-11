package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.NativeWebRequest;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.SecretKeyManager;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.configuration.web.AuthArgumentResolver;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.test.user.fixture.UserFixture;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@DisplayName("[UnitTest] AuthArgumentResolver 검증 단위 테스트")
class AuthArgumentResolverTest {

    private TokenProvider tokenProvider;
    private AuthArgumentResolver resolver;
    private UserCacheReadUseCase userCacheReadUseCase;
    private NativeWebRequest webRequest;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(
            "secretKey",
            "payloadSecretKey",
            "salt",
            900,
            1_209_600
        );
        final SecretKeyManager secretKeyManager = new SecretKeyManager(jwtProperties);
        tokenProvider = new TokenProvider(jwtProperties, secretKeyManager);
        userCacheReadUseCase = mock(UserCacheReadUseCase.class);
        resolver = new AuthArgumentResolver(userCacheReadUseCase, tokenProvider);
        request = mock(HttpServletRequest.class);
        webRequest = mock(NativeWebRequest.class);
        when(webRequest.getNativeRequest()).thenReturn(request);
    }

    @Test
    @DisplayName("ID와 토큰정보가 존재하고, 올바르다면 인증 객체가 생성된다.")
    void whenTokenAndUserIdIsValidThenResultShouldBeNotNull() {
        final UserJpaEntity user = UserFixture.createUser(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId());
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Access-Token", token.accessToken());
        when(request.getCookies()).thenReturn(cookies);
        when(userCacheReadUseCase.findById(user.getId()))
            .thenReturn(new UserCache(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getRoleAsString()
            ));

        final DailygeUser result = (DailygeUser) resolver.resolveArgument(null, null, webRequest, null);

        assertNotNull(result);
    }

    @Test
    @DisplayName("토큰이 없다면, UnAuthorizedException이 발생한다.")
    void whenTokenIsEmptyThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
            .isExactlyInstanceOf(CommonException.from(UN_AUTHORIZED).getClass())
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("사용자 ID가 유효하면 예외가 발생하지 않는다.")
    void whenUserIdIsValidThenResultShouldNotThrowException() {
        final Long validUserId = 456L;
        final UserJpaEntity expectedUser = UserFixture.createUser(validUserId);
        final DailygeToken token = tokenProvider.createToken(expectedUser.getId());
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Access-Token", token.accessToken());
        when(request.getCookies()).thenReturn(cookies);
        when(userCacheReadUseCase.findById(validUserId))
            .thenReturn(new UserCache(
                expectedUser.getId(),
                expectedUser.getNickname(),
                expectedUser.getEmail(),
                expectedUser.getProfileImageUrl(),
                expectedUser.getRoleAsString()
            ));

        assertDoesNotThrow(
            () -> ((DailygeUser) resolver.resolveArgument(null, null, webRequest, null))
        );
    }
}
