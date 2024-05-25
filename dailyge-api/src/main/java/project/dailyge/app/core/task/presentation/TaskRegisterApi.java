package project.dailyge.app.core.task.presentation;

import jakarta.validation.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import project.dailyge.app.common.auth.*;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.*;
import project.dailyge.app.common.response.*;
import project.dailyge.app.core.task.dto.requesst.*;
import project.dailyge.app.core.task.dto.response.*;
import project.dailyge.app.core.task.facade.*;
import project.dailyge.domain.task.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskRegisterApi {

    private final TaskFacade taskFacade;

    @PostMapping
    public ApiResponse<TaskRegisterResponse> registerTask(
        @LoginUser DailygeUser dailygeUser,
        @Valid @RequestBody TaskRegisterRequest request
    ) {
        Task newTask = taskFacade.save(request.toEntity(dailygeUser));
        TaskRegisterResponse payload = new TaskRegisterResponse(newTask);
        return ApiResponse.from(CREATED, payload);
    }
}
