package project.dailyge.app.core.user.external.oauth;

public enum OAuthClient {

    GOOGLE("Google");

    private final String code;

    OAuthClient(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
