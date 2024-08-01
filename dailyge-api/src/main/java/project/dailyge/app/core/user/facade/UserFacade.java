package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserWriteUseCase userWriteUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;

    public DailygeToken login(final String code) throws CommonException {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Long newUserSequence = userWriteUseCase.getSequence();
        final DailygeToken token = tokenProvider.createToken(newUserSequence, response.getEmail());
        tokenManager.saveRefreshToken(newUserSequence, token.refreshToken());
        return token;
    }

    public void logout(final Long userId) {
        if (userId != null) {
            tokenManager.deleteRefreshToken(userId);
        }
    }
}
