package project.dailyge.app.core.monthly_goal.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.monthly_goal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;

@ApplicationLayer
@RequiredArgsConstructor
public class MonthlyGoalWriteService implements MonthlyGoalWriteUseCase {

    private final MonthlyGoalEntityWriteRepository monthlyGoalWriteRepository;

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final MonthlyGoalCreateCommand command
    ) {
        final MonthlyGoalJpaEntity newMonthlyGoal = command.toEntity(dailygeUser);
        return monthlyGoalWriteRepository.save(newMonthlyGoal);
    }
}
