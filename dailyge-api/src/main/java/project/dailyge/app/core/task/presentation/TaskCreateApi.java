package project.dailyge.app.core.task.presentation;

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
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.MonthlyTasksCreateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import project.dailyge.app.core.task.presentation.response.TaskCreateResponse;

@RequiredArgsConstructor
@RequestMapping(path = "/api")
@PresentationLayer(value = "TaskCreateApi")
public class TaskCreateApi {

    private final TaskFacade taskFacade;
    private final TaskWriteService taskWriteService;

    @PostMapping(path = {"/monthly-tasks"})
    public ApiResponse<Void> createMonthlyTasks(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final MonthlyTasksCreateRequest request
    ) {
        taskFacade.createMonthlyTasks(dailygeUser, request.date());
        return ApiResponse.from(CREATED);
    }

    @PostMapping(path = {"/tasks"})
    public ApiResponse<TaskCreateResponse> registerTask(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final TaskCreateRequest request
    ) {
        final Long newTaskId = taskWriteService.save(dailygeUser, request.toCommand());
        final TaskCreateResponse payload = new TaskCreateResponse(newTaskId);
        return ApiResponse.from(CREATED, payload);
    }
}
