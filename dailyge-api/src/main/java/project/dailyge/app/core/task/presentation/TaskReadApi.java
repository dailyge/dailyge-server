package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskIdResponse;
import project.dailyge.app.core.task.presentation.response.MonthlyTaskResponse;
import project.dailyge.app.core.task.presentation.response.TaskDetailResponse;
import project.dailyge.app.core.task.presentation.response.TaskStatusResponse;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
public class TaskReadApi {

    private final TaskReadUseCase taskReadUseCase;

    @GetMapping(path = {"/monthly-tasks/id"})
    public ApiResponse<MonthlyTaskIdResponse> findMonthlyTaskId(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final Long findMonthlyTaskId = taskReadUseCase.findMonthlyTaskId(dailygeUser, date);
        final MonthlyTaskIdResponse payload = new MonthlyTaskIdResponse(findMonthlyTaskId);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks"})
    public ApiResponse<MonthlyTaskResponse> findMonthlyTasksByDate(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(value = "date") final LocalDate date
    ) {
        final List<TaskDetailResponse> findTasks = taskReadUseCase.findTasksByMonthlyTasksIdAndDate(dailygeUser, date).stream()
            .map(TaskDetailResponse::from)
            .toList();
        final MonthlyTaskResponse payload = MonthlyTaskResponse.from(date, findTasks);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = {"/tasks/{taskId}"})
    public ApiResponse<TaskDetailResponse> findTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "taskId") final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadUseCase.findTaskById(dailygeUser, taskId);
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
