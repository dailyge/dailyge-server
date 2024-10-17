package project.dailyge.app.core.retrospect.presentation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectPageResponse;
import project.dailyge.app.core.retrospect.presentation.response.RetrospectResponse;
import project.dailyge.app.page.CustomPageable;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_UN_RESOLVED_EXCEPTION;

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
        final CompletableFuture<List<RetrospectResponse>> retrospectsFuture = CompletableFuture.supplyAsync(() ->
            retrospectReadService.findRetrospectByPage(dailygeUser, page).stream()
                .map(RetrospectResponse::new)
                .toList());

        final CompletableFuture<Long> totalCountFuture = CompletableFuture.supplyAsync(() ->
            retrospectReadService.findTotalCount(dailygeUser)
        );

        final CompletableFuture<RetrospectPageResponse> payloadFuture = CompletableFuture.allOf(retrospectsFuture, totalCountFuture)
            .thenApply(ignored -> {
                final List<RetrospectResponse> retrospects = retrospectsFuture.join();
                final Long totalPageCount = page.getTotalPageCount(totalCountFuture.join());
                return new RetrospectPageResponse(retrospects, totalPageCount);
            }).exceptionally(ex -> {
                throw RetrospectTypeException.from(RETROSPECT_UN_RESOLVED_EXCEPTION);
            });

        return ApiResponse.from(OK, payloadFuture.join());
    }
}
