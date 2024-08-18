package project.dailyge.app.core.monthlygoal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalStatusUpdateRequest;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalUpdateRequest;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
public class MonthlyGoalUpdateApi {

    private final MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @PutMapping(path = {"/{monthlyGoalId}"})
    public ApiResponse<Void> updateMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId,
        @Valid @RequestBody MonthlyGoalUpdateRequest request
    ) {
        monthlyGoalWriteUseCase.update(dailygeUser, monthlyGoalId, request.toCommand());
        return ApiResponse.from(OK);
    }

    @PatchMapping(path = {"/{monthlyGoalId}/status"})
    public ApiResponse<Void> updateMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId,
        @Valid @RequestBody MonthlyGoalStatusUpdateRequest request
    ) {
        monthlyGoalWriteUseCase.update(dailygeUser, monthlyGoalId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
