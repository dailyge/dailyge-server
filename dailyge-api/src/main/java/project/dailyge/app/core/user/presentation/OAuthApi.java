package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/oauth2")
public class OAuthApi {

    private final UserFacade userFacade;

    @GetMapping
    public ApiResponse<OAuthLoginResponse> oAuthLogin(@RequestParam("code") final String code) {
        final DailygeToken dailygeToken = userFacade.oAuthLogin(code);

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, dailygeToken.getRefreshTokenCookie());

        OAuthLoginResponse payload = new OAuthLoginResponse(dailygeToken.accessToken());
        return ApiResponse.from(CommonCodeAndMessage.OK, headers, payload);
    }
}
