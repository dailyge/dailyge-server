package project.dailyge.app.core.anniversary.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryCreateRequest;
import project.dailyge.app.core.anniversary.presentation.response.AnniversaryCreateResponse;
import project.dailyge.app.core.common.auth.DailygeUser;

@RequestMapping(path = "/api/anniversaries")
@PresentationLayer(value = "AnniversaryCreateApi")
public class AnniversaryCreateApi {

    private final AnniversaryFacade anniversaryFacade;

    public AnniversaryCreateApi(final AnniversaryFacade anniversaryFacade) {
        this.anniversaryFacade = anniversaryFacade;
    }

    @PostMapping
    public ApiResponse<AnniversaryCreateResponse> createAnniversary(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final AnniversaryCreateRequest request
    ) {
        final Long newAnniversaryId = anniversaryFacade.save(dailygeUser, request.toCommand());
        final AnniversaryCreateResponse payload = new AnniversaryCreateResponse(newAnniversaryId);
        return ApiResponse.from(CREATED, payload);
    }
}
