package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.Presentation;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.presentation.response.UserInfoResponse;
import project.dailyge.entity.user.UserJpaEntity;

@Presentation
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserReadApi {

    private final UserReadUseCase userReadUseCase;

    @GetMapping(path = "/{userId}")
    public ApiResponse<UserInfoResponse> findUserById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "userId") final Long userId
    ) {
        dailygeUser.validateAuth(userId);
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(userId);
        final UserInfoResponse payload = new UserInfoResponse(findUser);
        return ApiResponse.from(OK, payload);
    }
}
