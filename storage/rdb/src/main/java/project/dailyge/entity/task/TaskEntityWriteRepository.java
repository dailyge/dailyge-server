package project.dailyge.entity.task;

import java.util.List;

public interface TaskEntityWriteRepository {
    Long save(TaskJpaEntity task);

    void saveBulks(List<TaskJpaEntity> tasks);
}
