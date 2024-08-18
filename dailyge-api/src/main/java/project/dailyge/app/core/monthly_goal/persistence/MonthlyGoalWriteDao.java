package project.dailyge.app.core.monthly_goal.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;

@Repository
@RequiredArgsConstructor
public class MonthlyGoalWriteDao implements MonthlyGoalEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final MonthlyGoalJpaEntity monthlyGoal) {
        entityManager.persist(monthlyGoal);
        return monthlyGoal.getId();
    }
}
