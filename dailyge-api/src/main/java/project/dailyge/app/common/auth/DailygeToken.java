package project.dailyge.app.common.auth;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int maxAge
) {
    private static final String BEARER =  "Bearer ";

    public String getAuthorizationToken() {
        return BEARER + accessToken;
    }

    public String getRefreshTokenCookie() {
        StringBuilder builder = new StringBuilder();
        builder.append("refreshToken=").append(refreshToken).append(";");
        builder.append("HttpOnly=true;");
        builder.append("Secure=true;");
        builder.append("Max-Age=").append(maxAge).append(";");
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
