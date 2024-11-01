package project.dailyge.app.core.task.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.presentation.requesst.TaskRecurrenceCreateRequest;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;

@RequestMapping(path = {"/api"})
@RestController
@PresentationLayer(value = "TaskRecurrenceCreateApi")
public class TaskRecurrenceCreateApi {

    private final TaskRecurrenceWriteService taskRecurrenceWriteService;

    public TaskRecurrenceCreateApi(final TaskRecurrenceWriteService taskRecurrenceWriteService) {
        this.taskRecurrenceWriteService = taskRecurrenceWriteService;
    }

    @PostMapping(path = "/task-recurrences")
    public ApiResponse<Void> createTaskRecurrences(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final TaskRecurrenceCreateRequest request
    ) {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, request.toCommand());
        return ApiResponse.from(CREATED);
    }
}
