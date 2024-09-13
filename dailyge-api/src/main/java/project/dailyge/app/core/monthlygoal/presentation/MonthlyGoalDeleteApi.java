package project.dailyge.app.core.monthlygoal.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;

@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
@PresentationLayer(value = "MonthlyGoalDeleteApi")
public class MonthlyGoalDeleteApi {

    private final MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @DeleteMapping(path = {"/{monthlyGoalId}"})
    public ApiResponse<Void> deleteMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId
    ) {
        monthlyGoalWriteUseCase.delete(dailygeUser, monthlyGoalId);
        return ApiResponse.from(NO_CONTENT);
    }
}
