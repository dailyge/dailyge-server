package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.app.core.user.presentation.response.LoginPageUrlResponse;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;

@RestController
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
        headers.add(SET_COOKIE, dailygeToken.getRefreshTokenCookie());
        final OAuthLoginResponse payload = new OAuthLoginResponse(dailygeToken.accessToken());
        return ApiResponse.from(OK, headers, payload);
    }

    @PostMapping(path = "/logout")
    public ApiResponse<Void> logout(@LoginUser final DailygeUser dailygeUser) {
        userFacade.logout(dailygeUser.getUserId());
        final HttpHeaders headers = new HttpHeaders();
        final ResponseCookie expiredRefreshTokenCookie = ResponseCookie.from("Refresh-Token")
            .value(null)
            .maxAge(0L)
            .build();
        headers.add(SET_COOKIE, expiredRefreshTokenCookie.toString());
        return ApiResponse.from(OK, headers, null);
    }
}
