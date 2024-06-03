package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.application.facade.UserFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/delete")
public class UserDeleteApi {

    private final UserFacade userFacade;
    private final UserValidator userValidator;

    @DeleteMapping("/{userId}")
    public ApiResponse userDelete(
        @LoginUser DailygeUser dailygeUser,
        @PathVariable(name = "userId") Long userId
    ) {
        userValidator.validate(dailygeUser.getUserId(), userId);
        userFacade.delete(userId);
        return ApiResponse.from(CommonCodeAndMessage.OK, null);
    }
}
