package project.dailyge.entity.goal;

import java.util.List;
import java.util.Optional;

public interface MonthlyGoalEntityReadRepository {
    Optional<MonthlyGoalJpaEntity> findById(Long monthlyGoalId);

    List<MonthlyGoalJpaEntity> findByUserIdAndYearAndMonth(Long userId, int year, int month);
}
