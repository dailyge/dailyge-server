package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.Presentation;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.validation.UuidFormat;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskIdResponse;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskResponse;
import project.dailyge.app.core.task.presentation.response.TaskDocumentResponse;
import project.dailyge.app.core.task.presentation.response.TaskStatusResponse;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Presentation
@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
public class TaskReadApi {

    private final TaskReadUseCase taskReadUseCase;

    @GetMapping(path = {"/monthly-tasks/id"})
    public ApiResponse<MonthlyTaskIdResponse> findMonthlyTaskIdByUserIdAndDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, date);
        final MonthlyTaskIdResponse payload = new MonthlyTaskIdResponse(findMonthlyTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/monthly-tasks"})
    public ApiResponse<MonthlyTaskResponse> findMonthlyTaskByUserIdAndDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, date);
        final MonthlyTaskResponse payload = MonthlyTaskResponse.from(findMonthlyTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/monthly-tasks/{monthlyTaskId}"})
    public ApiResponse<MonthlyTaskResponse> findMonthlyTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @UuidFormat @PathVariable(value = "monthlyTaskId") final String monthlyTaskId
    ) {
        final MonthlyTaskDocument findMonthlyTask = taskReadUseCase.findMonthlyTaskById(dailygeUser, monthlyTaskId);
        final MonthlyTaskResponse payload = MonthlyTaskResponse.from(findMonthlyTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/{taskId}"})
    public ApiResponse<TaskDocumentResponse> findTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @UuidFormat @PathVariable(value = "taskId") final String taskId,
        @RequestParam(name = "date") final LocalDate date
    ) {
        final TaskDocument findTask = taskReadUseCase.findByIdAndDate(dailygeUser, taskId, date);
        final TaskDocumentResponse payload = TaskDocumentResponse.from(findTask);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/status"})
    public ApiResponse<List<TaskStatusResponse>> findTaskStatus(@LoginUser final DailygeUser dailygeUser) {
        final List<TaskStatusResponse> payload = Arrays.stream(TaskStatus.values())
            .map(TaskStatusResponse::from)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
