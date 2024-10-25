package project.dailyge.entity.weeklygoal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeeklyGoalEntityReadRepository {

    Optional<WeeklyGoalJpaEntity> findById(Long weeklyGoalId);

    List<WeeklyGoalJpaEntity> findByUserIdAndWeekStartDateByLimit(Long userId, LocalDateTime weekStartDate, int limit);

    List<WeeklyGoalJpaEntity> findByCursor(Long userId, long index, int limit, LocalDateTime weekStartDate);
}
