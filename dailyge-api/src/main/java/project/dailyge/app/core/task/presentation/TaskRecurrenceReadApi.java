package project.dailyge.app.core.task.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceReadService;
import project.dailyge.app.core.task.presentation.response.TaskRecurrenceDetailResponse;
import project.dailyge.app.paging.Cursor;

import java.util.List;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@RestController
@RequestMapping(path = {"/api/task-recurrences"})
@PresentationLayer(value = "TaskRecurrenceReadApi")
public class TaskRecurrenceReadApi {

    private final TaskRecurrenceReadService taskRecurrenceReadService;

    public TaskRecurrenceReadApi(final TaskRecurrenceReadService taskRecurrenceReadService) {
        this.taskRecurrenceReadService = taskRecurrenceReadService;
    }

    @GetMapping()
    public ApiResponse<List<TaskRecurrenceDetailResponse>> findTaskRecurrencesByCursor(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor
    ) {
        final List<TaskRecurrenceDetailResponse> payload = taskRecurrenceReadService.findTaskRecurrencesByCursor(dailygeUser, cursor)
            .stream().map(TaskRecurrenceDetailResponse::from)
            .toList();
        return ApiResponse.from(OK, payload);
    }
}
