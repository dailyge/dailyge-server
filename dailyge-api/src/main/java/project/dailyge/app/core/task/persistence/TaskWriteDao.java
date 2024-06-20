package project.dailyge.app.core.task.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;

@Repository
@RequiredArgsConstructor
class TaskWriteDao implements TaskEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public TaskJpaEntity save(final TaskJpaEntity task) {
        entityManager.persist(task);
        return task;
    }
}
