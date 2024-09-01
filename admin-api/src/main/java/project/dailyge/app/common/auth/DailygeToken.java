package project.dailyge.app.common.auth;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int accessTokenMaxAge,
    int refreshTokenMaxAge
) {

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
