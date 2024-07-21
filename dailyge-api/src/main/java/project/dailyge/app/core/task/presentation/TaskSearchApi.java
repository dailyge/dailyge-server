package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.validation.UuidFormat;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskIdResponse;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskResponse;
import project.dailyge.app.core.task.presentation.response.TaskDetailResponse;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
public class TaskSearchApi {

    private final TaskReadUseCase taskReadUseCase;

    @GetMapping(path = {"/monthly-tasks"})
    public ApiResponse<MonthlyTaskIdResponse> findMonthlyTaskByUserIdAndDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, date);
        final MonthlyTaskIdResponse payload = new MonthlyTaskIdResponse(findMonthlyTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/monthly-tasks/{monthlyTaskId}"})
    public ApiResponse<MonthlyTaskResponse> findMonthlyTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @UuidFormat @PathVariable(value = "monthlyTaskId") final String monthlyTaskId
    ) {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskById(dailygeUser, monthlyTaskId);
        final MonthlyTaskResponse payload = new MonthlyTaskResponse(findMonthlyTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/{taskId}"})
    public ApiResponse<TaskDetailResponse> findTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "taskId") final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadUseCase.findById(dailygeUser, taskId);
        final TaskDetailResponse payload = new TaskDetailResponse(findTask);
        return ApiResponse.from(OK, payload);
    }
}
