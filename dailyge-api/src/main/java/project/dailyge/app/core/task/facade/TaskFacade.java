package project.dailyge.app.core.task.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.user.UserJpaEntity;

@Component
@RequiredArgsConstructor
public class TaskFacade {

    private final UserReadUseCase userReadUseCase;
    private final TaskWriteUseCase taskWriteUseCase;

    public TaskJpaEntity save(final TaskJpaEntity task) {
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(task.getUserId());
        return taskWriteUseCase.save(task);
    }

    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(dailygeUser.getUserId());
        taskWriteUseCase.delete(dailygeUser, taskId);
    }
}
