package project.dailyge.app.core.monthlygoal.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.cursor.Cursor;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadUseCase;
import project.dailyge.app.core.monthlygoal.presentation.response.MonthlyGoalResponse;

import java.util.List;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
public class MonthlyGoalReadApi {

    private final MonthlyGoalReadUseCase monthlyGoalReadUseCase;

    @GetMapping
    public ApiResponse<List<MonthlyGoalResponse>> findMonthlyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor,
        @RequestParam(required = false) final int year,
        @RequestParam(required = false) final int month
    ) {
        final List<MonthlyGoalResponse> payload = monthlyGoalReadUseCase.findMonthlyGoalsByCursor(dailygeUser, cursor, year, month).stream()
            .map(MonthlyGoalResponse::new)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
