package project.dailyge.app.core.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.application.UserWriteUseCase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserDeleteApi {

    private final UserWriteUseCase userWriteUseCase;

    @DeleteMapping(path = "/{userId}")
    public ApiResponse<Void> userDelete(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "userId") final Long userId
    ) {
        dailygeUser.validateAuth(userId);
        userWriteUseCase.delete(userId);
        return ApiResponse.from(NO_CONTENT, null);
    }
}
