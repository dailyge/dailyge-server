package project.dailyge.entity.task;

import java.util.Optional;

public interface TaskLabelEntityReadRepository {
    Optional<TaskLabelJpaEntity> findTaskLabelById(Long id);

    long countTaskLabel(Long userId);
}
