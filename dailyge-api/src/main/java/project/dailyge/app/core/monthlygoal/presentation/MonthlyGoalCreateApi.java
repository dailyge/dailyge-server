package project.dailyge.app.core.monthlygoal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalCreateRequest;
import project.dailyge.app.core.monthlygoal.presentation.response.MonthlyGoalCreateResponse;

@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
@PresentationLayer(value = "MonthlyGoalCreateApi")
public class MonthlyGoalCreateApi {

    private final MonthlyGoalWriteService monthlyGoalWriteService;

    @PostMapping
    public ApiResponse<MonthlyGoalCreateResponse> createMonthlyGoal(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final MonthlyGoalCreateRequest request
    ) {
        final Long monthlyGoalId = monthlyGoalWriteService.save(dailygeUser, request.toCommand());
        final MonthlyGoalCreateResponse payload = new MonthlyGoalCreateResponse(monthlyGoalId);
        return ApiResponse.from(CREATED, payload);
    }
}
