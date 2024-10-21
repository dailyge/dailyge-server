package project.dailyge.app.core.weeklygoal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.presentation.request.WeeklyGoalStatusUpdateRequest;
import project.dailyge.app.core.weeklygoal.presentation.request.WeeklyGoalUpdateRequest;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequiredArgsConstructor
@RequestMapping(path = "/api/weekly-goals")
@PresentationLayer(value = "WeeklyGoalUpdateApi")
public class WeeklyGoalUpdateApi {

    private final WeeklyGoalWriteService weeklyGoalWriteService;

    @PutMapping(path = {"/{weeklyGoalId}"})
    public ApiResponse<Void> updateWeeklyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "weeklyGoalId") final Long weeklyGoalId,
        @Valid @RequestBody final WeeklyGoalUpdateRequest request
    ) {
        weeklyGoalWriteService.update(dailygeUser, weeklyGoalId, request.toCommand());
        return ApiResponse.from(OK);
    }

    @PatchMapping(path = {"/{weeklyGoalId}/status"})
    public ApiResponse<Void> updateWeeklyGoalStatusById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "weeklyGoalId") final Long weeklyGoalId,
        @Valid @RequestBody WeeklyGoalStatusUpdateRequest request
    ) {
        weeklyGoalWriteService.update(dailygeUser, weeklyGoalId, request.done());
        return ApiResponse.from(OK);
    }
}
