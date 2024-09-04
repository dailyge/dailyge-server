package project.dailyge.app.core.task.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import project.dailyge.app.core.task.presentation.requesst.TaskStatusUpdateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = {"/api/tasks"})
public class TaskUpdateApi {

    private final TaskWriteUseCase taskWriteUseCase;

    @PutMapping(path = {"/{taskId}"})
    public ApiResponse<Void> update(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId,
        @Valid @RequestBody final TaskUpdateRequest request
    ) {
        final TaskUpdateCommand command = request.toCommand();
        taskWriteUseCase.update(dailygeUser, taskId, command);
        return ApiResponse.from(OK);
    }

    @PatchMapping(path = {"/{taskId}/status"})
    public ApiResponse<Void> updateStatus(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId,
        @Valid @RequestBody final TaskStatusUpdateRequest request
    ) {
        final TaskStatusUpdateCommand command = request.toCommand();
        taskWriteUseCase.updateStatus(dailygeUser, taskId, command);
        return ApiResponse.from(OK);
    }
}
