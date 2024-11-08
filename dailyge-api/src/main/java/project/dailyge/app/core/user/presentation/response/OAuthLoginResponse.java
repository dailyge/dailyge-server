package project.dailyge.app.core.user.presentation.response;

public class OAuthLoginResponse {

    private final String accessToken;

    public OAuthLoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return String.format("accessToken: %s", accessToken);
    }
}
