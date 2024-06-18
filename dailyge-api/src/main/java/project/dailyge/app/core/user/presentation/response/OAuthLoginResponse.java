package project.dailyge.app.core.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginResponse {

    private String accessToken;

    @Override
    public String toString() {
        return String.format("accessToken: %s", accessToken);
    }
}
