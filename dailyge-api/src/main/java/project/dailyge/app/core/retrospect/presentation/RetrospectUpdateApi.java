package project.dailyge.app.core.retrospect.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectUpdateRequest;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectUpdateApi")
public class RetrospectUpdateApi {

    private final RetrospectWriteService retrospectWriteService;

    @PutMapping(path = "/{retrospectId}")
    public ApiResponse<Void> update(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "retrospectId") final Long retrospectId,
        @Valid @RequestBody final RetrospectUpdateRequest request
    ) {
        retrospectWriteService.update(dailygeUser, request.toCommand(), retrospectId);
        return ApiResponse.from(OK);
    }
}
