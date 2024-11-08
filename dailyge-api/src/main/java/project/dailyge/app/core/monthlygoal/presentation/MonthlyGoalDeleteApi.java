package project.dailyge.app.core.monthlygoal.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;

@RequestMapping(path = "/api/monthly-goals")
@PresentationLayer(value = "MonthlyGoalDeleteApi")
public class MonthlyGoalDeleteApi {

    private final MonthlyGoalWriteService monthlyGoalWriteService;

    public MonthlyGoalDeleteApi(final MonthlyGoalWriteService monthlyGoalWriteService) {
        this.monthlyGoalWriteService = monthlyGoalWriteService;
    }

    @DeleteMapping(path = {"/{monthlyGoalId}"})
    public ApiResponse<Void> deleteMonthlyGoalById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "monthlyGoalId") final Long monthlyGoalId
    ) {
        monthlyGoalWriteService.delete(dailygeUser, monthlyGoalId);
        return ApiResponse.from(NO_CONTENT);
    }
}
