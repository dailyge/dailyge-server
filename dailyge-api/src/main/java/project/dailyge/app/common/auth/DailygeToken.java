package project.dailyge.app.common.auth;

import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int accessTokenMaxAge,
    int refreshTokenMaxAge
) {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    public String getAccessTokenCookie() {
        return createResponseCookie(ACCESS_TOKEN, accessToken, "/", accessTokenMaxAge);
    }

    public String getRefreshTokenCookie() {
        return createResponseCookie(REFRESH_TOKEN, refreshToken, "/", refreshTokenMaxAge);
    }

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
