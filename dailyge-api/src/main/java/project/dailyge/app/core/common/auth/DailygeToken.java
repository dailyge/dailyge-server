package project.dailyge.app.core.common.auth;

import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int accessTokenMaxAge,
    int refreshTokenMaxAge
) {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    public String getAccessTokenCookie(final String env) {
        return createResponseCookie(ACCESS_TOKEN, accessToken, "/", accessTokenMaxAge, true, env);
    }

    public String getRefreshTokenCookie(final String env) {
        return createResponseCookie(REFRESH_TOKEN, refreshToken, "/", refreshTokenMaxAge, true, env);
    }

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
