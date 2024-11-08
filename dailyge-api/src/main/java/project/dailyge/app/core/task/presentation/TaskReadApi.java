package project.dailyge.app.core.task.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadService;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskIdResponse;
import project.dailyge.app.core.task.presentation.response.TaskDetailResponse;
import project.dailyge.app.core.task.presentation.response.TaskStatusResponse;
import project.dailyge.app.core.task.presentation.response.TasksResponse;
import project.dailyge.app.core.task.presentation.validator.TaskClientValidator;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequestMapping(path = {"/api"})
@PresentationLayer(value = "TaskReadApi")
public class TaskReadApi {

    private final TaskClientValidator validator;
    private final TaskReadService taskReadService;

    public TaskReadApi(
        final TaskClientValidator validator,
        final TaskReadService taskReadService
    ) {
        this.validator = validator;
        this.taskReadService = taskReadService;
    }

    @GetMapping(path = {"/monthly-tasks/id"})
    public ApiResponse<MonthlyTaskIdResponse> findMonthlyTaskId(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final Long findMonthlyTaskId = taskReadService.findMonthlyTaskId(dailygeUser, date);
        final MonthlyTaskIdResponse payload = new MonthlyTaskIdResponse(findMonthlyTaskId);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks"})
    public ApiResponse<TasksResponse> findTasksByDates(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "startDate") final LocalDate startDate,
        @RequestParam(value = "endDate") final LocalDate endDate
    ) {
        validator.validateFromStartDateToEndDate(startDate, endDate);
        final List<TaskDetailResponse> findWeeklyTasks =
            taskReadService.findTasksByMonthlyTaskIdAndDates(dailygeUser, startDate, endDate).stream()
                .map(TaskDetailResponse::from)
                .toList();
        final TasksResponse payload = new TasksResponse(findWeeklyTasks);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/{taskId}"})
    public ApiResponse<TaskDetailResponse> findTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "taskId") final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadService.findTaskById(dailygeUser, taskId);
        final TaskDetailResponse payload = TaskDetailResponse.from(findTask);
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
