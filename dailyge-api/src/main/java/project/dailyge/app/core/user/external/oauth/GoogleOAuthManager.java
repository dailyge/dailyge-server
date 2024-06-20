package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

@Component
@RequiredArgsConstructor
public class GoogleOAuthManager {

    private final GoogleAccessClient googleAccessClient;
    private final GoogleUserInfoClient googleUserInfoClient;

    public GoogleUserInfoResponse login(final String code) {
        final GoogleAuthorizationResponse response = googleAccessClient.userAccess(code);
        return googleUserInfoClient.getUserInfo(response);
    }
}
