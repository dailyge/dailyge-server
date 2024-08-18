package project.dailyge.app.core.monthly_goal.application;

import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;

public interface MonthlyGoalReadUseCase {
    MonthlyGoalJpaEntity findById(Long monthlyGoalId);
}
