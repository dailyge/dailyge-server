package project.dailyge.app.core.user.presentation;

import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import static project.dailyge.app.common.utils.CookieUtils.clearResponseCookie;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.facade.UserFacade;

@RequestMapping("/api/users")
@PresentationLayer(value = "UserDeleteApi")
public class UserDeleteApi {

    private final UserFacade userFacade;

    public UserDeleteApi(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @DeleteMapping(path = "/{userId}")
    public ApiResponse<Void> userDelete(
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
