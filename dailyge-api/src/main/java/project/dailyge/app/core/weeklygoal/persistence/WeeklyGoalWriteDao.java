package project.dailyge.app.core.weeklygoal.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.goal.WeeklyGoalEntityWriteRepository;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

@Repository
class WeeklyGoalWriteDao implements WeeklyGoalEntityWriteRepository {

    private final EntityManager entityManager;

    public WeeklyGoalWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final WeeklyGoalJpaEntity weeklyGoal) {
        entityManager.persist(weeklyGoal);
        return weeklyGoal.getId();
    }
}
