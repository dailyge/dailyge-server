package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import static project.dailyge.app.common.utils.CookieUtils.clearResponseCookie;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserDeleteApi {

    private final UserFacade userFacade;

    @DeleteMapping(path = "/{userId}")
    public ApiResponse<Void> delete (
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "userId") final Long userId
    ) {
        dailygeUser.validateAuth(userId);
        userFacade.delete(userId);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, clearResponseCookie("Access-Token", true));
        headers.add(SET_COOKIE, clearResponseCookie("Refresh-Token", true));
        headers.add(SET_COOKIE, clearResponseCookie("Logged-In", false));
        return ApiResponse.from(NO_CONTENT, headers, null);
    }
}
