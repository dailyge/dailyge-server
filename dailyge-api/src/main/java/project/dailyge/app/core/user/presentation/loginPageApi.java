package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.presentation.response.LoginPageUrlResponse;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_ALREADY_LOGGED_IN;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class loginPageApi {

    @Value("${oauth.google.url}")
    private String loginUrl;
    private final static String DEFAULT_REFERER = "/";

    @GetMapping
    public ApiResponse<LoginPageUrlResponse> login(
        @RequestHeader(value = "referer", required = false) final String referer,
        @LoginUser final DailygeUser dailygeUser
    ) {
        if (dailygeUser != null) {
            final LoginPageUrlResponse payload = new LoginPageUrlResponse(
                    referer != null ? referer : DEFAULT_REFERER);
            return ApiResponse.from(USER_ALREADY_LOGGED_IN, payload);
        }
        final LoginPageUrlResponse payload = new LoginPageUrlResponse(loginUrl);
        return ApiResponse.from(OK, payload);
    }
}
