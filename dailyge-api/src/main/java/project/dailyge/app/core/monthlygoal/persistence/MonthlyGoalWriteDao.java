package project.dailyge.app.core.monthlygoal.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

@Repository
@RequiredArgsConstructor
class MonthlyGoalWriteDao implements MonthlyGoalEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final MonthlyGoalJpaEntity monthlyGoal) {
        entityManager.persist(monthlyGoal);
        return monthlyGoal.getId();
    }
}
