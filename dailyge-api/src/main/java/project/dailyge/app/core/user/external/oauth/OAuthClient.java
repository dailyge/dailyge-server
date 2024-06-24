package project.dailyge.app.core.user.external.oauth;

public enum OAuthClient {

    GOOGLE("google");

    private final String code;

    OAuthClient(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
