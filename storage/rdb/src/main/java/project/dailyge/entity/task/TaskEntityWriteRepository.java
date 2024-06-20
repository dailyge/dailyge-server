package project.dailyge.entity.task;

import project.dailyge.entity.task.TaskJpaEntity;

public interface TaskEntityWriteRepository {
    TaskJpaEntity save(TaskJpaEntity task);
}
