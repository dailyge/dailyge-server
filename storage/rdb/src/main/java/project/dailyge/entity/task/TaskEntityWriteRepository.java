package project.dailyge.entity.task;

public interface TaskEntityWriteRepository {
    Long save(TaskJpaEntity task);
}
