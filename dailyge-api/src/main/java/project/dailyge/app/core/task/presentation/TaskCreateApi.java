package project.dailyge.app.core.task.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.MonthlyTasksRegisterRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.task.presentation.response.TaskRegisterResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class TaskCreateApi {

    private final TaskFacade taskFacade;

    @PostMapping(path = {"/monthly-tasks"})
    public ApiResponse<Void> createMonthlyTasks(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final MonthlyTasksRegisterRequest request
    ) {
        taskFacade.createMonthlyTasks(dailygeUser, request.date());
        return ApiResponse.from(CREATED);
    }

    @PostMapping(path = {"/tasks"})
    public ApiResponse<TaskRegisterResponse> registerTask(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final TaskRegisterRequest request
    ) {
        final String newTaskId = taskFacade.save(dailygeUser, request.toCommand());
        final TaskRegisterResponse payload = new TaskRegisterResponse(newTaskId);
        return ApiResponse.from(CREATED, payload);
    }
}
