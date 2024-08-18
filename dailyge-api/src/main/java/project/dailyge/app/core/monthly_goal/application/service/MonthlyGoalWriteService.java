package project.dailyge.app.core.monthly_goal.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.monthly_goal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalCreateCommand;
import static project.dailyge.app.core.monthly_goal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthly_goal.exception.MonthlyGoalTypeException;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;

@ApplicationLayer
@RequiredArgsConstructor
public class MonthlyGoalWriteService implements MonthlyGoalWriteUseCase {

    private final MonthlyGoalEntityReadRepository monthlyGoalReadRepository;
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

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long monthlyGoalId
    ) {
        final MonthlyGoalJpaEntity findMonthlyGoal = monthlyGoalReadRepository.findById(monthlyGoalId)
            .orElseThrow(() -> MonthlyGoalTypeException.from(MONTHLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findMonthlyGoal.getUserId())) {
            throw new UnAuthorizedException();
        }
        findMonthlyGoal.delete();
    }
}
