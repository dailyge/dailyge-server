package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.task.TaskJpaEntity;

import java.util.Optional;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findById(Long taskId);
}
