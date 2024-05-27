package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.task.TaskJpaEntity;

public interface TaskEntityWriteRepository {
    TaskJpaEntity save(TaskJpaEntity task);
}
