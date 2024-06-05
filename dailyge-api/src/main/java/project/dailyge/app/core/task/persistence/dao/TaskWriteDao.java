package project.dailyge.app.core.task.persistence.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.domain.task.TaskEntityWriteRepository;
import project.dailyge.domain.task.TaskJpaEntity;

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
