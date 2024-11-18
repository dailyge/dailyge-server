package project.dailyge.app.core.task.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;

@RequestMapping(path = {"/api/task-recurrences"})
public class TaskRecurrenceDeleteApi {

    private final TaskRecurrenceWriteService taskRecurrenceWriteService;

    public TaskRecurrenceDeleteApi(final TaskRecurrenceWriteService taskRecurrenceWriteService) {
        this.taskRecurrenceWriteService = taskRecurrenceWriteService;
    }

    @DeleteMapping(path = {"/{taskRecurrenceId}"})
    public ApiResponse<Void> deleteTaskRecurrence(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskRecurrenceId") final Long taskRecurrenceId
    ) {
        taskRecurrenceWriteService.delete(dailygeUser, taskRecurrenceId);
        return ApiResponse.from(NO_CONTENT);
    }
}
