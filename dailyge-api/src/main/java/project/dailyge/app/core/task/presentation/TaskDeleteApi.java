package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.presentation.response.TaskCreateResponse;

@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@PresentationLayer(value = "TaskDeleteApi")
public class TaskDeleteApi {

    private final TaskWriteUseCase taskWriteUseCase;

    @DeleteMapping(path = {"/{taskId}"})
    public ApiResponse<TaskCreateResponse> deleteTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId
    ) {
        taskWriteUseCase.delete(dailygeUser, taskId);
        return ApiResponse.from(NO_CONTENT);
    }
}
