package project.dailyge.app.core.monthlygoal.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadService;
import project.dailyge.app.core.monthlygoal.presentation.response.MonthlyGoalResponse;
import project.dailyge.app.paging.Cursor;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
@PresentationLayer(value = "MonthlyGoalReadApi")
public class MonthlyGoalReadApi {

    private final MonthlyGoalReadService monthlyGoalReadService;

    @GetMapping
    public ApiResponse<List<MonthlyGoalResponse>> findMonthlyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor,
        @RequestParam(name = "year", required = false) final Integer year,
        @RequestParam(name = "month", required = false) final Integer month
    ) {
        final List<MonthlyGoalResponse> payload = monthlyGoalReadService.findMonthlyGoalsByCursor(dailygeUser, cursor, year, month).stream()
            .map(MonthlyGoalResponse::new)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
