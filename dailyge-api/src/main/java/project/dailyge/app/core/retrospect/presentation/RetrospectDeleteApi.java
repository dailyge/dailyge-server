package project.dailyge.app.core.retrospect.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectDeleteApi")
public class RetrospectDeleteApi {

    private final RetrospectWriteService retrospectWriteService;

    @DeleteMapping(path = "/{retrospectId}")
    public ApiResponse<Void> delete(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "retrospectId") final Long retrospectId
    ) {
        retrospectWriteService.delete(dailygeUser, retrospectId);
        return ApiResponse.from(NO_CONTENT);
    }
}
