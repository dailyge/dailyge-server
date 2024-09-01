package project.dailyge.app.common.auth;

public record DailygeToken(
    String accessToken,
    String refreshToken,
    int maxAge
) {

    private static final String REFRESH_TOKEN = "Refresh-Token";

    @Override
    public String toString() {
        return String.format("{accessToken: %s, refreshToken: %s}", accessToken, refreshToken);
    }
}
