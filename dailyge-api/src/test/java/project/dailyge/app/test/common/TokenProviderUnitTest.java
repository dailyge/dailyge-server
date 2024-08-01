package project.dailyge.app.test.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.SecretKeyManager;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.test.user.fixture.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[UnitTest] TokenProvider 단위 테스트")
class TokenProviderUnitTest {

    private static final int REFRESH_EXPIRED_TIME = 2;
    private static final String ACCESS_TOKEN = "access_token";

    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(
            "secretKey",
            "payloadSecretKey",
            "salt",
            1,
            REFRESH_EXPIRED_TIME
        );
        final SecretKeyManager secretKeyManager = new SecretKeyManager(jwtProperties);
        tokenProvider = new TokenProvider(jwtProperties, secretKeyManager);
    }

    @Test
    @DisplayName("사용자 토큰을 생성하면, 토큰이 정상적으로 생성된다.")
    void whenCreateUserTokenThenResultShouldBeNotNull() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId(), user.getEmail());

        assertAll(
            () -> assertNotNull(token),
            () -> assertDoesNotThrow(() -> tokenProvider.getUserId(token.accessToken())),
            () -> assertDoesNotThrow(() -> tokenProvider.getUserId(token.refreshToken())),
            () -> assertEquals(REFRESH_EXPIRED_TIME * 3600, token.maxAge())
        );
    }

    @Test
    @DisplayName("사용자 토큰이 올바르다면, 사용자 ID를 얻는다.")
    void whenUserTokenCorrectThenUserIdShouldBeNotNull() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId(), user.getEmail());

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
    @DisplayName("빈 토큰을 검증하면, UnAuthorizedException 가 발생한다.")
    void whenEmptyTokenThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId(null))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("서명이 다른 토큰으로 검증하면, UnAuthorizedException 가 발생한다.")
    void whenDifferentSignatureThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.test"))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("토큰 형식이 올바르지 않는다면, UnAuthorizedException 가 발생한다.")
    void whenTokenFormatIncorrectThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("test"))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("지원되지 않는 형식의 JWT 일 경우, UnAuthorizedException 가 발생한다.")
    void whenUnsupportedTokenThenUnAuthorizedExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenProvider.getUserId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test."))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @DisplayName("사용자 ID를 암호화 하면, 정상적으로 암호화 된다.")
    @ValueSource(longs = {1L, 100L, 2231234124L})
    void whenEncryptUserIdThenUserIdShouldBeEncrypted(final Long userId) {
        final String encryptedUserId = tokenProvider.encryptUserId(userId);

        assertEquals(44, encryptedUserId.length());
    }

    @Test
    @DisplayName("암호화 된 사용자 ID를 Decrypt 하면, 정상적으로 사용자 ID를 얻을 수 있다.")
    void whenDecodeTheEncryptedUserIDThenResultShouldBeUserId() {
        final Long userId = 1L;
        final String encryptedUserId = tokenProvider.encryptUserId(userId);

        final Long decryptUserId = tokenProvider.decryptUserId(encryptedUserId);

        assertEquals(userId, decryptUserId);
    }

    @Test
    @DisplayName("비정상적인 페이로드가 올 경우, UnAuthorizedException 이 발생한다.")
    void whenAbnormalPayloadThenUnAuthorizedExceptionShouldBeHappen() {
        final String abnormalPayload = "jwtAbnormalPayloadData";

        assertThatThrownBy(() -> tokenProvider.decryptUserId(abnormalPayload))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }
}
