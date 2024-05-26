package project.dailyge.app.core.task.facade;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import project.dailyge.app.core.task.application.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.domain.task.*;
import project.dailyge.domain.user.*;

@Component
@RequiredArgsConstructor
public class TaskFacade {

    private final UserReadUseCase userReadUseCase;
    private final TaskWriteUseCase taskWriteUseCase;

    @Transactional
    public TaskJpaEntity save(TaskJpaEntity task) {
        User findUser = userReadUseCase.findById(task.getUserId());
        return taskWriteUseCase.save(task);
    }
}
