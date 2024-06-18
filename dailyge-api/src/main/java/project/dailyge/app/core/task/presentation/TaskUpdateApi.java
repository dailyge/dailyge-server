package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import project.dailyge.app.core.task.presentation.response.TaskRegisterResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskUpdateApi {

    private final TaskWriteUseCase taskWriteUseCase;

    @PutMapping(path = {"/{taskId}"})
    public ApiResponse<TaskRegisterResponse> update(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId,
        @RequestBody final TaskUpdateRequest request
    ) {
        TaskUpdateCommand command = new TaskUpdateCommand(request.title(), request.content(), request.date());
        taskWriteUseCase.update(dailygeUser, taskId, command);
        return ApiResponse.from(NO_CONTENT);
    }
}
