package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.task.*;

import java.util.*;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findById(Long taskId);
}
