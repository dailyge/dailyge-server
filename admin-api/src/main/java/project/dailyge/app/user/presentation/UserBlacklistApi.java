package project.dailyge.app.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.user.facade.UserFacade;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user/blacklist")
public class UserBlacklistApi {

    private final UserFacade userFacade;

    @PostMapping(path = "/{userId}")
    public ApiResponse<Void> userInvalidate(@PathVariable(name = "userId") final Long userId) {
        userFacade.invalidateUser(userId);
        return ApiResponse.from(OK);
    }
}
