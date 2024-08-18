package project.dailyge.app.core.monthly_goal.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.monthly_goal.application.MonthlyGoalReadUseCase;
import static project.dailyge.app.core.monthly_goal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthly_goal.exception.MonthlyGoalTypeException;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;

@ApplicationLayer
@RequiredArgsConstructor
public class MonthlyGoalReadService implements MonthlyGoalReadUseCase {

    private final MonthlyGoalEntityReadRepository monthlyGoalReadRepository;

    @Override
    public MonthlyGoalJpaEntity findById(final Long monthlyGoalId) {
        return monthlyGoalReadRepository.findById(monthlyGoalId)
            .orElseThrow(() -> MonthlyGoalTypeException.from(MONTHLY_GOAL_NOT_FOUND));
    }
}
