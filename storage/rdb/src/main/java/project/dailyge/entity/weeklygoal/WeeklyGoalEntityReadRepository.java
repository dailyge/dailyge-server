package project.dailyge.entity.weeklygoal;

import java.util.Optional;

public interface WeeklyGoalEntityReadRepository {

    Optional<WeeklyGoalJpaEntity> findById(Long weeklyGoalId);
}
