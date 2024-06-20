package project.dailyge.app.core.task.persistence;

import project.dailyge.entity.task.TaskJpaEntity;

public interface TaskEntityWriteRepository {
    TaskJpaEntity save(TaskJpaEntity task);
}
