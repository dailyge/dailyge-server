package project.dailyge.app.core.task.application;

import project.dailyge.domain.task.TaskJpaEntity;

public interface TaskWriteUseCase {
    TaskJpaEntity save(TaskJpaEntity task);
}
