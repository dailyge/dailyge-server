package project.dailyge.app.core.user.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.app.core.user.presentation.request.UserUpdateRequest;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserUpdateApi {

    private final UserFacade userFacade;

    @PatchMapping(path = "/{userId}")
    public ApiResponse<Void> updateUser(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable("userId") final Long userId,
        @Valid @RequestBody final UserUpdateRequest request
    ) {
        dailygeUser.validateAuth(userId);
        userFacade.updateUser(userId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
