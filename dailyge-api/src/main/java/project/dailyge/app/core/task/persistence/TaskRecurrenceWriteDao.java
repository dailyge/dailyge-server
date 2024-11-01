package project.dailyge.app.core.task.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.task.TaskRecurrenceEntityWriteRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

@Repository
public class TaskRecurrenceWriteDao implements TaskRecurrenceEntityWriteRepository {

    private final EntityManager entityManager;

    public TaskRecurrenceWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final TaskRecurrenceJpaEntity taskRecurrence) {
        entityManager.persist(taskRecurrence);
        return taskRecurrence.getId();
    }
}
