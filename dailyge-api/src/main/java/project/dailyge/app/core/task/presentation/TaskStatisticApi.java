package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.presentation.response.MonthlyWeeksStatisticResponse;
import project.dailyge.app.core.task.presentation.response.WeeklyTasksStatisticResponse;
import project.dailyge.app.core.task.presentation.validator.TaskClientValidator;
import project.dailyge.entity.task.Tasks;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
@PresentationLayer(value = "TaskStatisticApi")
public class TaskStatisticApi {

    private final TaskClientValidator validator;
    private final TaskReadUseCase taskReadUseCase;

    @GetMapping(path = {"/tasks/statistic"})
    public ApiResponse<WeeklyTasksStatisticResponse> findWeeklyTasksStatisticByUserIdAndDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "startDate") final LocalDate startDate,
        @RequestParam(value = "endDate") final LocalDate endDate
    ) {
        validator.validateFromStartDateToEndDate(startDate, endDate);
        final Tasks weeklyTasks = taskReadUseCase.findTasksStatisticByUserIdAndDate(dailygeUser, startDate, endDate);
        final WeeklyTasksStatisticResponse payload = new WeeklyTasksStatisticResponse(startDate, endDate, weeklyTasks);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/statistic/monthly-weeks"})
    public ApiResponse<MonthlyWeeksStatisticResponse> findMonthlyTasksStatisticByUserIdAndDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "startDate") final LocalDate startDate,
        @RequestParam(value = "endDate") final LocalDate endDate
    ) {
        validator.validateOneMonthDifference(startDate, endDate);
        final Tasks monthlyTasks = taskReadUseCase.findTasksStatisticByUserIdAndDate(dailygeUser, startDate, endDate);
        final MonthlyWeeksStatisticResponse payload = new MonthlyWeeksStatisticResponse(startDate, endDate, monthlyTasks);
        return ApiResponse.from(OK, payload);
    }
}
