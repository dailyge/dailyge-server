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
import project.dailyge.app.core.task.presentation.response.TaskRecurrenceIdResponse;
import project.dailyge.app.core.task.presentation.validator.TaskRecurrenceClientValidator;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;

@RequestMapping(path = {"/api"})
@RestController
@PresentationLayer(value = "TaskRecurrenceCreateApi")
public class TaskRecurrenceCreateApi {

    private final TaskRecurrenceClientValidator validator;
    private final TaskRecurrenceWriteService taskRecurrenceWriteService;

    public TaskRecurrenceCreateApi(
        final TaskRecurrenceWriteService taskRecurrenceWriteService,
        final TaskRecurrenceClientValidator validator
    ) {
        this.taskRecurrenceWriteService = taskRecurrenceWriteService;
        this.validator = validator;
    }

    @PostMapping(path = {"/task-recurrences"})
    public ApiResponse<TaskRecurrenceIdResponse> createTaskRecurrences(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final TaskRecurrenceCreateRequest request
    ) {
        validator.validateStartDateToEndDate(request.getStartDate(), request.getEndDate());
        validator.validateDayPattern(request.getType(), request.getDatePattern());
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, request.toCommand());
        final TaskRecurrenceIdResponse payload = new TaskRecurrenceIdResponse(taskRecurrenceId);
        return ApiResponse.from(CREATED, payload);
    }
}
