package project.dailyge.app.common.auth;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int maxAge
) {
    public String getRefreshTokenCookie() {
        StringBuilder builder = new StringBuilder();
        builder.append("refreshToken=" + refreshToken + ";");
        builder.append("HttpOnly=true;");
        builder.append("Secure=true;");
        builder.append("Max-Age=" + maxAge + ";");
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
