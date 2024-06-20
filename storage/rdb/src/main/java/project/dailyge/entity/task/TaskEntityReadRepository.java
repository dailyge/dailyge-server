package project.dailyge.entity.task;

import project.dailyge.entity.task.TaskJpaEntity;

import java.util.Optional;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findById(Long taskId);
}
