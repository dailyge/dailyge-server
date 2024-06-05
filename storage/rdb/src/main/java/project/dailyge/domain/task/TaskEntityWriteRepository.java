package project.dailyge.domain.task;

import project.dailyge.domain.task.TaskJpaEntity;

public interface TaskEntityWriteRepository {
    TaskJpaEntity save(TaskJpaEntity task);
}
