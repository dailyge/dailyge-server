package project.dailyge.app.core.retrospect.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectPageResponse;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.response.AsyncPagingResponse;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@RequestMapping(path = "/api/retrospects")
@PresentationLayer(value = "RetrospectReadApi")
public class RetrospectReadApi {

    private final RetrospectReadService retrospectReadService;

    public RetrospectReadApi(final RetrospectReadService retrospectReadService) {
        this.retrospectReadService = retrospectReadService;
    }

    @GetMapping
    public ApiResponse<RetrospectPageResponse> findMonthlyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @OffsetPageable final CustomPageable page
    ) {
        final AsyncPagingResponse<RetrospectJpaEntity> retrospectByPage = retrospectReadService.findRetrospectAndTotalCountByPage(dailygeUser, page);
        final int totalPageCount = page.getTotalPageCount(retrospectByPage.totalCount());
        final RetrospectPageResponse payload = new RetrospectPageResponse(retrospectByPage.data(), totalPageCount);

        return ApiResponse.from(OK, payload);
    }
}
