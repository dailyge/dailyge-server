package project.dailyge.app.core.task.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.presentation.response.TaskCreateResponse;

import java.time.LocalDate;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskDeleteApi {

    private final TaskWriteUseCase taskWriteUseCase;

    @DeleteMapping(path = {"/{taskId}"})
    public ApiResponse<TaskCreateResponse> deleteTaskById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "taskId") final Long taskId,
        @RequestParam(name = "date") final LocalDate date
    ) {
        taskWriteUseCase.delete(dailygeUser, taskId, date);
        return ApiResponse.from(NO_CONTENT);
    }
}
