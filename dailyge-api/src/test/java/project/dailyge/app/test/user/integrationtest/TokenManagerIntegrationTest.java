package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.external.oauth.TokenManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.EMPTY_USER_ID;

@DisplayName("[IntegrationTest] TokenManager 통합 테스트")
class TokenManagerIntegrationTest extends DatabaseTestBase {

    public static final String REFRESH_TOKEN = "Refresh-Token";

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("리프레시 토큰을 저장하는데 성공한다.")
    void whenSavingRefreshTokenThenRefreshTokenShouldBeExists() {
        tokenManager.saveRefreshToken(1L, REFRESH_TOKEN);

        final String refreshTokenKey = tokenManager.getRefreshToken(1L);

        Assertions.assertEquals(REFRESH_TOKEN, refreshTokenKey);
    }

    @Test
    @DisplayName("저장 시 ID가 null 이면, 토큰을 저장하는데 실패한다.")
    void whenSavingRefreshTokenWithIdIsNullThenExternalServerExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenManager.saveRefreshToken(null, REFRESH_TOKEN))
            .isExactlyInstanceOf(UserTypeException.from(EMPTY_USER_ID).getClass())
            .isInstanceOf(RuntimeException.class)
            .hasMessage(EMPTY_USER_ID.message());
    }

    @Test
    @DisplayName("저장 시 토큰정보가 null이면, 토큰을 저장하는데 실패한다.")
    void whenSavingRefreshTokenIsNullThenExternalServerExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenManager.saveRefreshToken(1L, null))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Value must not be null");
    }

    @Test
    @DisplayName("검색 시 ID가 null 이면, 토큰을 검색하는데 실패한다.")
    void whenSearchRefreshTokenWithIdIsNullThenExternalServerExceptionShouldBeHappen() {
        assertThatThrownBy(() -> tokenManager.getRefreshToken(null))
            .isExactlyInstanceOf(UserTypeException.from(EMPTY_USER_ID).getClass())
            .isInstanceOf(RuntimeException.class)
            .hasMessage(EMPTY_USER_ID.message());
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
