package project.dailyge.app.core.task.persistence;

import project.dailyge.entity.task.TaskJpaEntity;

import java.util.Optional;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findById(Long taskId);
}
