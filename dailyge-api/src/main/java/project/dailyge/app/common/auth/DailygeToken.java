package project.dailyge.app.common.auth;

import org.springframework.http.ResponseCookie;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int maxAge
) {
    private static final String BEARER =  "Bearer ";
    private static final String REFRESH_TOKEN =  "refreshToken";

    public String getAuthorizationToken() {
        return BEARER + accessToken;
    }

    public String getRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .httpOnly(true)
            .secure(true)
            .maxAge(maxAge)
            .build()
            .toString();
    }

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
