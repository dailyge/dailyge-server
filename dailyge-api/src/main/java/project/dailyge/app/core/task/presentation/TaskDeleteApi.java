package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.dto.response.TaskRegisterResponse;
import project.dailyge.app.core.task.facade.TaskFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskDeleteApi {

    private final TaskFacade taskFacade;

    @DeleteMapping(path = {"/{taskId}"})
    public ApiResponse<TaskRegisterResponse> deleteTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId
    ) {
        taskFacade.delete(dailygeUser, taskId);
        return ApiResponse.from(NO_CONTENT);
    }
}
