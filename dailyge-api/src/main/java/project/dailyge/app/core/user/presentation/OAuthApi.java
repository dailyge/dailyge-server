package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/oauth2")
public class OAuthApi {

    private final UserFacade userFacade;

    @GetMapping
    public ApiResponse<OAuthLoginResponse> login(@RequestParam("code") final String code) {
        final DailygeToken dailygeToken = userFacade.login(code);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, dailygeToken.getRefreshTokenCookie());
        final OAuthLoginResponse payload = new OAuthLoginResponse(dailygeToken.accessToken());
        return ApiResponse.from(OK, headers, payload);
    }
}
