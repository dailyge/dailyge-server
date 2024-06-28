package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.presentation.response.LoginPageUrlResponse;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginPageApi {

    @Value("${oauth.google.url}")
    private String loginUrl;

    @GetMapping
    public ApiResponse<LoginPageUrlResponse> login() {
        final LoginPageUrlResponse payload = new LoginPageUrlResponse(loginUrl);
        return ApiResponse.from(OK, payload);
    }
}
