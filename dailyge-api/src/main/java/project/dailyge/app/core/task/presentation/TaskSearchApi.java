package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.presentation.response.TaskDetailResponse;
import project.dailyge.entity.task.TaskJpaEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskSearchApi {

    private final TaskReadUseCase taskReadUseCase;

    @GetMapping(path = {"/{taskId}"})
    public ApiResponse<TaskDetailResponse> findTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "taskId") final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadUseCase.findById(dailygeUser, taskId);
        final TaskDetailResponse payload = new TaskDetailResponse(findTask);
        return ApiResponse.from(OK, payload);
    }
}
