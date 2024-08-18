package project.dailyge.entity.monthlygoal;

import java.util.Optional;

public interface MonthlyGoalEntityReadRepository {
    Optional<MonthlyGoalJpaEntity> findById(Long monthlyGoalId);
}
