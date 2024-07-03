package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.external.oauth.TokenManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static project.dailyge.app.common.exception.ExternalServerException.REDIS_SAVE_FAILED_MESSAGE;
import static project.dailyge.app.common.exception.ExternalServerException.REDIS_SEARCH_FAILED_MESSAGE;

@DisplayName("[IntegrationTest] TokenManager 통합 테스트")
class TokenManagerIntegrationTest extends DatabaseTestBase {

    public static final String REFRESH_TOKEN = "Refresh-Token";

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("리프레시 토큰을 저장하는데 성공한다.")
    void whenSavingRefreshTokenThenRefreshTokenShouldBeExists() {
        tokenManager.saveRefreshToken(1L, REFRESH_TOKEN);

        final String refreshTokenKey = tokenManager.getRefreshTokenKey(1L);

        Assertions.assertEquals(REFRESH_TOKEN, refreshTokenKey);
    }

    @Test
    @DisplayName("저장 시 ID가 null 이면, 토큰을 저장하는데 실패한다.")
    void whenSavingRefreshTokenWithIdIsNullThenExternalServerExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenManager.saveRefreshToken(null, REFRESH_TOKEN))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(REDIS_SAVE_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("검색 시 ID가 null 이면, 토큰을 검색하는데 실패한다.")
    void whenSearchRefreshTokenWithIdIsNullThenExternalServerExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenManager.getRefreshTokenKey(null))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(REDIS_SEARCH_FAILED_MESSAGE);
    }
}
