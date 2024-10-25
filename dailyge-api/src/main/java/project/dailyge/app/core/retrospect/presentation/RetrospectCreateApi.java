package project.dailyge.app.core.retrospect.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectCreateResponse;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectCreateApi")
public class RetrospectCreateApi {

    private final RetrospectWriteService retrospectWriteService;

    @PostMapping
    public ApiResponse<RetrospectCreateResponse> createRetrospect(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final RetrospectCreateRequest request
    ) {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, request.toCommand());
        final RetrospectCreateResponse payload = new RetrospectCreateResponse(retrospectId);
        return ApiResponse.from(CREATED, payload);
    }
}
