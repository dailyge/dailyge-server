package project.dailyge.app.core.weeklygoal.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityWriteRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

@Repository
@RequiredArgsConstructor
public class WeeklyGoalWriteDao implements WeeklyGoalEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final WeeklyGoalJpaEntity weeklyGoal) {
        entityManager.persist(weeklyGoal);
        return weeklyGoal.getId();
    }
}
