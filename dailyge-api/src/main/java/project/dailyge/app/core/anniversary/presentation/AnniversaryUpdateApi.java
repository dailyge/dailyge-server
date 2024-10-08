package project.dailyge.app.core.anniversary.presentation;

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
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryUpdateRequest;
import project.dailyge.app.core.anniversary.presentation.response.AnniversaryCreateResponse;
import project.dailyge.app.core.common.auth.DailygeUser;

@RequiredArgsConstructor
@RequestMapping(path = "/api/anniversaries")
@PresentationLayer(value = "AnniversaryUpdateApi")
public class AnniversaryUpdateApi {

    private final AnniversaryFacade anniversaryFacade;

    @PutMapping(path = "/{anniversaryId}")
    public ApiResponse<AnniversaryCreateResponse> updateAnniversary(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "anniversaryId") final Long anniversaryId,
        @Valid @RequestBody final AnniversaryUpdateRequest request
    ) {
        anniversaryFacade.update(dailygeUser, anniversaryId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
