package project.dailyge.app.test.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.JWTAuthenticationException;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static project.dailyge.app.common.exception.JWTAuthenticationException.*;

@DisplayName("[UnitTest] TokenProvider 단위 테스트")
class TokenProviderUnitTest {

    private static final int REFRESH_EXPIRED_TIME = 2;
    private static final String ACCESS_TOKEN = "access_token";

    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties("secretKey", 1, REFRESH_EXPIRED_TIME);
        tokenProvider = new TokenProvider(jwtProperties);
    }

    @Test
    @DisplayName("사용자 토큰을 생성하면, 토큰이 정상적으로 생성된다.")
    void whenCreateUserTokenThenResultShouldBeNotNull() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);

        assertAll(
            () ->  assertNotNull(token),
            () ->  assertDoesNotThrow(() -> tokenProvider.getUserId(token.accessToken())),
            () ->  assertDoesNotThrow(() -> tokenProvider.getUserId(token.refreshToken())),
            () ->  assertEquals(REFRESH_EXPIRED_TIME * 3600, token.maxAge())
        );
    }
    
    @Test
    @DisplayName("사용자 토큰이 올바르다면, 사용자 ID를 얻는다.")
    void whenUserTokenCorrectThenUserIdShouldBeNotNull() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);

        assertEquals(user.getId(), tokenProvider.getUserId(token.accessToken()));
    }

    @Test
    @DisplayName("authorizationHeader 가 존재하면, AccessToken 을 반환한다.")
    void whenAuthorizationHeaderExistsThenResultShouldBeAccessToken() {
        final String authorizationHeader = "Bearer " + ACCESS_TOKEN;
        final String accessToken = tokenProvider.getAccessToken(authorizationHeader);

        assertEquals(ACCESS_TOKEN, accessToken);
    }

    @Test
    @DisplayName("빈 토큰을 검증하면, JWTAuthenticationException 가 발생한다.")
    void whenEmptyTokenThenJWTAuthenticationExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId(null))
            .isExactlyInstanceOf(JWTAuthenticationException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(EMPTY_TOKEN_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("서명이 다른 토큰으로 검증하면, JWTAuthenticationException 가 발생한다.")
    void whenDifferentSignatureThenJWTAuthenticationExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.test"))
            .isExactlyInstanceOf(JWTAuthenticationException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(TOKEN_SIGNATURE_VERIFICATION_FAILED_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("토큰 형식이 올바르지 않는다면, JWTAuthenticationException 가 발생한다.")
    void whenTokenFormatIncorrectThenJWTAuthenticationExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("test"))
            .isExactlyInstanceOf(JWTAuthenticationException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(TOKEN_FORMAT_INCORRECT_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("지원되지 않는 형식의 JWT 일 경우, JWTAuthenticationException 가 발생한다.")
    void whenUnsupportedTokenThenJWTAuthenticationExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test."))
            .isExactlyInstanceOf(JWTAuthenticationException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(UNSUPPORTED_TOKEN_ERROR_MESSAGE);
    }
}
