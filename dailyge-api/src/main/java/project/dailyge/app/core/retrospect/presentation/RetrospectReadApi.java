package project.dailyge.app.core.retrospect.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectResponse;
import project.dailyge.app.page.Page;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectReadApi")
public class RetrospectReadApi {

    private final RetrospectReadService retrospectReadService;

    @GetMapping
    public ApiResponse<List<RetrospectResponse>> findMonthlyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @OffsetPageable final Page page
    ) {
        final List<RetrospectResponse> payload = retrospectReadService.findRetrospectByPage(dailygeUser, page).stream()
            .map(RetrospectResponse::new)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
