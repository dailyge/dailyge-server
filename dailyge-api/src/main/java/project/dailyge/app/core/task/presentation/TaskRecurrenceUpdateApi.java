package project.dailyge.app.core.task.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.presentation.requesst.TaskRecurrenceUpdateRequest;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RequestMapping(path = {"/api/task-recurrences"})
@PresentationLayer(value = "TaskRecurrenceUpdateApi")
public class TaskRecurrenceUpdateApi {

    private final TaskRecurrenceWriteService taskRecurrenceWriteService;

    public TaskRecurrenceUpdateApi(final TaskRecurrenceWriteService taskRecurrenceWriteService) {
        this.taskRecurrenceWriteService = taskRecurrenceWriteService;
    }

    @PutMapping(path = {"/{taskRecurrenceId}"})
    public ApiResponse<Void> updateTaskRecurrence(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskRecurrenceId") final Long taskRecurrenceId,
        @Valid @RequestBody final TaskRecurrenceUpdateRequest request
    ) {
        taskRecurrenceWriteService.update(dailygeUser, taskRecurrenceId, request.toCommand());
        return ApiResponse.from(OK);
    }
}
