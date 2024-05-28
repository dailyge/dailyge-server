package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.dto.response.UserInfoResponse;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserReadApi {

    private final UserReadUseCase userReadUseCase;
    private final UserValidator userVaildator;

    @GetMapping(path =  "/{userId}")
    public ApiResponse<UserInfoResponse> findUserById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable final Long userId
    ) {
        userVaildator.validate(dailygeUser.getUserId(), userId);
        UserInfoResponse payload = userReadUseCase.findUserInfoById(userId);
        return ApiResponse.from(OK, payload);
    }
}
