package project.dailyge.app.core.task.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.domain.task.TaskJpaEntity;
import project.dailyge.domain.user.User;

@Component
@RequiredArgsConstructor
public class TaskFacade {

    private final UserReadUseCase userReadUseCase;
    private final TaskWriteUseCase taskWriteUseCase;

    @Transactional
    public TaskJpaEntity save(final TaskJpaEntity task) {
        User findUser = userReadUseCase.findById(task.getUserId());
        return taskWriteUseCase.save(task);
    }
}
