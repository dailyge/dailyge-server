package project.dailyge.app.core.task.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.task.dto.requesst.TaskRegisterRequest;
import project.dailyge.app.core.task.dto.response.TaskRegisterResponse;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.domain.task.TaskJpaEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskRegisterApi {

    private final TaskFacade taskFacade;

    @PostMapping
    public ApiResponse<TaskRegisterResponse> registerTask(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final TaskRegisterRequest request
    ) {
        final TaskJpaEntity newTask = taskFacade.save(request.toEntity(dailygeUser));
        final TaskRegisterResponse payload = new TaskRegisterResponse(newTask);
        return ApiResponse.from(CREATED, payload);
    }
}
