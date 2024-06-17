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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/oauth2")
public class OAuthApi {

    private final UserFacade userFacade;

    @GetMapping
    public ApiResponse<Void> oAuthLogin(@RequestParam("code") String code) {
        DailygeToken dailygeToken = userFacade.oAuthLogin(code);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer " + dailygeToken.accessToken());
        headers.add(HttpHeaders.SET_COOKIE, dailygeToken.getRefreshTokenCookie());

        return ApiResponse.from(CommonCodeAndMessage.OK, headers,null);
    }
}
