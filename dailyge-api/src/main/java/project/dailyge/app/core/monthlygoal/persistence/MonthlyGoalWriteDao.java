package project.dailyge.app.core.monthlygoal.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.goal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.goal.MonthlyGoalJpaEntity;

@Repository
class MonthlyGoalWriteDao implements MonthlyGoalEntityWriteRepository {

    private final EntityManager entityManager;

    public MonthlyGoalWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final MonthlyGoalJpaEntity monthlyGoal) {
        entityManager.persist(monthlyGoal);
        return monthlyGoal.getId();
    }
}
