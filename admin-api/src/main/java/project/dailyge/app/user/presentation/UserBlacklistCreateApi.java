package project.dailyge.app.user.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.user.facade.UserFacade;
import project.dailyge.app.user.presentation.request.UserBlacklistCreateRequest;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/user/blacklist")
public class UserBlacklistCreateApi {

    private final UserFacade userFacade;

    @PostMapping(path = "/{userId}")
    public ApiResponse<Void> userInvalidate(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "userId") final Long userId,
        @Valid @RequestBody final UserBlacklistCreateRequest request
    ) {
        if (!dailygeUser.isAdmin()) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        userFacade.invalidateUser(userId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
