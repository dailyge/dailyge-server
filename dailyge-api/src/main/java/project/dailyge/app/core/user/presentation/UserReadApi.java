package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.presentation.response.UserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@PresentationLayer(value = "UserReadApi")
public class UserReadApi {

    private final UserCacheReadService userCacheReadService;

    @GetMapping(path = "/{userId}")
    public ApiResponse<UserInfoResponse> findUserById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "userId") final Long userId
    ) {
        dailygeUser.validateAuth(userId);
        final UserCache findUser = userCacheReadService.findById(userId);
        final UserInfoResponse payload = new UserInfoResponse(findUser);
        return ApiResponse.from(OK, payload);
    }
}
