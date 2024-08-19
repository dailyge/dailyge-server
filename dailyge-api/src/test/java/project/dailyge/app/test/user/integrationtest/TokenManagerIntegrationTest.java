package project.dailyge.app.test.user.integrationtest;

import io.lettuce.core.RedisConnectionException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.external.oauth.TokenManager;

@DisplayName("[IntegrationTest] TokenManager 통합 테스트")
class TokenManagerIntegrationTest extends DatabaseTestBase {

    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String CODE_AND_MESSAGE = "codeAndMessage";

    @Autowired
    private TokenManager tokenManager;

    private TokenManager mockTokenManager;
    private RedisTemplate<String, byte[]> mockStringRedisTemplate;

    @BeforeEach
    void setUp() {
        mockStringRedisTemplate = mock(RedisTemplate.class);
        mockTokenManager = new TokenManager(mockStringRedisTemplate);
    }

    @Test
    @DisplayName("리프레시 토큰을 저장하는데 성공한다.")
    void whenSaveRefreshTokenThenRefreshTokenShouldBeExists() {
        tokenManager.saveRefreshToken(1L, REFRESH_TOKEN);

        final String refreshTokenKey = tokenManager.getRefreshToken(1L);

        Assertions.assertEquals(REFRESH_TOKEN, refreshTokenKey);
    }

    @Test
    @DisplayName("레디스 예외가 발생하면, ExternalServerException이 발생한다.")
    void whenThrowRedisExceptionThenExternalServerExceptionShouldBeHappen() {
        when(mockStringRedisTemplate.opsForValue()).thenThrow(RedisConnectionException.class);

        assertThatThrownBy(() -> mockTokenManager.saveRefreshToken(1L, REFRESH_TOKEN))
            .isExactlyInstanceOf(ExternalServerException.class)
            .extracting(CODE_AND_MESSAGE)
            .isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("예상치 못한 예외가 발생하면, ExternalServerException이 발생한다.")
    void whenThrowUnexpectedExceptionThenExternalServerExceptionShouldBeHappen() {
        when(mockStringRedisTemplate.opsForValue()).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> mockTokenManager.saveRefreshToken(1L, REFRESH_TOKEN))
            .isExactlyInstanceOf(ExternalServerException.class)
            .extracting(CODE_AND_MESSAGE)
            .isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("리프레시 토큰을 삭제하면, 정삭적으로 삭제 된다.")
    void whenDeleteRefreshTokenThenRefreshTokenShouldBeNull() {
        tokenManager.saveRefreshToken(1L, REFRESH_TOKEN);
        tokenManager.deleteRefreshToken(1L);

        assertNull(tokenManager.getRefreshToken(1L));
    }

    @Test
    @DisplayName("로그인 되어있지 않는 사용자 ID로 삭제하여도, 에러가 발생하지 않는다.")
    void whenDeleteByNotLoggedInUserThenDoesNotThrowException() {
        assertDoesNotThrow(() -> tokenManager.deleteRefreshToken(1L));
    }
}
