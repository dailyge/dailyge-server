package project.dailyge.app.core.weeklygoal.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalReadService;
import project.dailyge.app.core.weeklygoal.presentation.validator.WeeklyGoalClientValidator;
import project.dailyge.app.core.weeklygoal.response.WeeklyGoalResponse;
import project.dailyge.app.paging.Cursor;

import java.time.LocalDate;
import java.util.List;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequiredArgsConstructor
@RequestMapping(path = "/api/weekly-goals")
@PresentationLayer(value = "WeeklyGoalReadApi")
public class WeeklyGoalReadApi {

    private final WeeklyGoalClientValidator validator;
    private final WeeklyGoalReadService weeklyGoalReadService;

    @GetMapping
    public ApiResponse<List<WeeklyGoalResponse>> findWeeklyGoalsByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor,
        @RequestParam(name = "weekStartDate")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate weekStartDate
    ) {
        validator.validateWeekStartDate(weekStartDate);
        final List<WeeklyGoalResponse> payload = weeklyGoalReadService
            .findPageByCursor(dailygeUser, cursor, weekStartDate.atTime(0, 0, 0, 0))
            .stream().map(WeeklyGoalResponse::new)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
