package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.common.utils.CookieUtils;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.app.core.user.presentation.response.LoginPageUrlResponse;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import static project.dailyge.app.common.utils.CookieUtils.clearResponseCookie;

@PresentationLayer
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginApi {

    @Value("${oauth.google.url}")
    private String loginUrl;

    private final UserFacade userFacade;

    @GetMapping(path = "/login")
    public ApiResponse<LoginPageUrlResponse> loginPage() {
        final LoginPageUrlResponse payload = new LoginPageUrlResponse(loginUrl);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = "/oauth2")
    public ApiResponse<OAuthLoginResponse> login(@RequestParam("code") final String code) {
        final DailygeToken dailygeToken = userFacade.login(code);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, dailygeToken.getAccessTokenCookie());
        headers.add(SET_COOKIE, dailygeToken.getRefreshTokenCookie());
        headers.add(SET_COOKIE, CookieUtils.createResponseCookie(
            "Logged-In", "yes", "/", dailygeToken.accessTokenMaxAge(), false
        ));
        final OAuthLoginResponse payload = new OAuthLoginResponse(dailygeToken.accessToken());
        return ApiResponse.from(OK, headers, payload);
    }

    @PostMapping(path = "/logout")
    public ApiResponse<Void> logout(@LoginUser final DailygeUser dailygeUser) {
        userFacade.logout(dailygeUser.getUserId());
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, clearResponseCookie("Access-Token", true));
        headers.add(SET_COOKIE, clearResponseCookie("Refresh-Token", true));
        headers.add(SET_COOKIE, clearResponseCookie("Logged-In", false));
        return ApiResponse.from(OK, headers, null);
    }
}
