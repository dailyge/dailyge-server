package project.dailyge.app.core.retrospect.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectCreateResponse;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectCreateApi")
public class RetrospectCreateApi {

    private final RetrospectWriteService retrospectWriteService;

    @PostMapping
    public ApiResponse<RetrospectCreateResponse> create(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final RetrospectCreateRequest request
    ) {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, request.toCommand());
        final RetrospectCreateResponse payload = new RetrospectCreateResponse(retrospectId);
        return ApiResponse.from(CREATED, payload);
    }
}
