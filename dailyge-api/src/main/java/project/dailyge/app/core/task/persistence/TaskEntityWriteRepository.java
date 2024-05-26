package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.task.*;

public interface TaskEntityWriteRepository {
    TaskJpaEntity save(TaskJpaEntity task);
}
