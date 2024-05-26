package project.dailyge.app.core.task.application;

import project.dailyge.domain.task.*;

public interface TaskWriteUseCase {
    TaskJpaEntity save(TaskJpaEntity task);
}
