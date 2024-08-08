package project.dailyge.app.core.user.presentation.request;

public record OAuthLoginRequest(String code) {

    @Override
    public String toString() {
        return String.format("code: %s", code);
    }
}
