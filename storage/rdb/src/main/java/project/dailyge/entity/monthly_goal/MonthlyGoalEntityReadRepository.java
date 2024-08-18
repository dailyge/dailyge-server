package project.dailyge.entity.monthly_goal;

import java.util.Optional;

public interface MonthlyGoalEntityReadRepository {
    Optional<MonthlyGoalJpaEntity> findById(Long monthlyGoalId);
}
