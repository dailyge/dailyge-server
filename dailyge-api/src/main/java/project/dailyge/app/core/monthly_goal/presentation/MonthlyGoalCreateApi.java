package project.dailyge.app.core.monthly_goal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.monthly_goal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthly_goal.presentation.request.MonthlyGoalCreateRequest;
import project.dailyge.app.core.monthly_goal.presentation.response.MonthlyGoalCreateResponse;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/monthly-goals")
public class MonthlyGoalCreateApi {

    private final MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @PostMapping
    public ApiResponse<MonthlyGoalCreateResponse> createMonthlyGoal(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final MonthlyGoalCreateRequest request
    ) {
        final Long monthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, request.toCommand());
        final MonthlyGoalCreateResponse payload = new MonthlyGoalCreateResponse(monthlyGoalId);
        return ApiResponse.from(CREATED, payload);
    }
}
