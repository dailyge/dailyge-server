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
import project.dailyge.app.core.retrospect.presentation.response.RetrospectPageResponse;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectResponse;
import project.dailyge.app.page.CustomPageable;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequiredArgsConstructor
@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectReadApi")
public class RetrospectReadApi {

    private final RetrospectReadService retrospectReadService;

    @GetMapping
    public ApiResponse<RetrospectPageResponse> findMonthlyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @OffsetPageable final CustomPageable page
    ) {
        final List<RetrospectResponse> retrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page).stream()
            .map(RetrospectResponse::new)
            .toList();
        final int totalPageCount = retrospectReadService.findTotalCount(dailygeUser);
        final RetrospectPageResponse payload = new RetrospectPageResponse(retrospects, page.getTotalPageCount(totalPageCount));
        return ApiResponse.from(OK, payload);
    }
}
