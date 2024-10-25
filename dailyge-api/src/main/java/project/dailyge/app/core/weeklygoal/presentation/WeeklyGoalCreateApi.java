package project.dailyge.app.core.weeklygoal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.presentation.request.WeeklyGoalCreateRequest;
import project.dailyge.app.core.weeklygoal.presentation.response.WeeklyGoalCreateResponse;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;

@RequiredArgsConstructor
@RequestMapping(path = "/api/weekly-goals")
@PresentationLayer(value = "WeeklyGoalCreateApi")
public class WeeklyGoalCreateApi {

    private final WeeklyGoalWriteService weeklyGoalWriteService;

    @PostMapping
    public ApiResponse<WeeklyGoalCreateResponse> createWeeklyGoal(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final WeeklyGoalCreateRequest request
    ) {
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, request.toCommand());
        final WeeklyGoalCreateResponse payload = new WeeklyGoalCreateResponse(weeklyGoalId);
        return ApiResponse.from(CREATED, payload);
    }
}
