package project.dailyge.domain.task;

import java.util.Optional;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findById(Long taskId);
}
