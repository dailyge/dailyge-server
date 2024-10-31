package project.dailyge.app.core.monthlygoal.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalStatusUpdateRequest;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalUpdateRequest;

@RequestMapping(path = "/api/monthly-goals")
@PresentationLayer(value = "MonthlyGoalUpdateApi")
public class MonthlyGoalUpdateApi {

    private final MonthlyGoalWriteService monthlyGoalWriteService;

    public MonthlyGoalUpdateApi(final MonthlyGoalWriteService monthlyGoalWriteService) {
        this.monthlyGoalWriteService = monthlyGoalWriteService;
    }

    @PutMapping(path = {"/{monthlyGoalId}"})
    public ApiResponse<Void> updateMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId,
        @Valid @RequestBody MonthlyGoalUpdateRequest request
    ) {
        monthlyGoalWriteService.update(dailygeUser, monthlyGoalId, request.toCommand());
        return ApiResponse.from(OK);
    }

    @PatchMapping(path = {"/{monthlyGoalId}/status"})
    public ApiResponse<Void> updateStatusMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId,
        @Valid @RequestBody MonthlyGoalStatusUpdateRequest request
    ) {
        monthlyGoalWriteService.update(dailygeUser, monthlyGoalId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
