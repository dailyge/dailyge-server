package project.dailyge.app.core.monthlygoal.application;

import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

public interface MonthlyGoalReadUseCase {
    MonthlyGoalJpaEntity findById(Long monthlyGoalId);
}
