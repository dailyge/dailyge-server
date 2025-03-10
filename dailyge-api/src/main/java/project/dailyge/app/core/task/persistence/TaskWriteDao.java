package project.dailyge.app.core.task.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.task.MonthlyTaskEntityWriteRepository;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.util.List;
import project.dailyge.entity.task.TaskLabelEntityWriteRepository;
import project.dailyge.entity.task.TaskLabelJpaEntity;

@Repository
class TaskWriteDao implements TaskEntityWriteRepository, MonthlyTaskEntityWriteRepository, TaskLabelEntityWriteRepository {

    private final EntityManager entityManager;

    public TaskWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final TaskJpaEntity task) {
        entityManager.persist(task);
        return task.getId();
    }

    /**
     * 운영 편의를 위해 만든 메서드로, 테스트를 제외한 다른 환경에서 호출하지 말 것.
     */
    @Override
    @Transactional
    public Long save(final MonthlyTaskJpaEntity monthlyTask) {
        entityManager.persist(monthlyTask);
        return monthlyTask.getId();
    }

    @Override
    public void saveAll(final List<MonthlyTaskJpaEntity> monthlyTasks) {
        for (final MonthlyTaskJpaEntity monthlyTask : monthlyTasks) {
            entityManager.persist(monthlyTask);
        }
    }

    @Override
    public Long save(final TaskLabelJpaEntity taskLabel) {
        entityManager.persist(taskLabel);
        return taskLabel.getId();
    }

    @Override
    public void saveBulks(final List<TaskJpaEntity> tasks) {
        for (final TaskJpaEntity task : tasks) {
            entityManager.persist(task);
        }
    }
}
