package project.dailyge.app.core.monthlygoal.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalStatusUpdateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalUpdateCommand;
import static project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

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

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long monthlyGoalId,
        final MonthlyGoalUpdateCommand command
    ) {
        final MonthlyGoalJpaEntity findMonthlyGoal = monthlyGoalReadRepository.findById(monthlyGoalId)
            .orElseThrow(() -> MonthlyGoalTypeException.from(MONTHLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findMonthlyGoal.getUserId())) {
            throw new UnAuthorizedException();
        }
        findMonthlyGoal.update(command.title(), command.content());
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long monthlyGoalId,
        final MonthlyGoalStatusUpdateCommand command
    ) {
        final MonthlyGoalJpaEntity findMonthlyGoal = monthlyGoalReadRepository.findById(monthlyGoalId)
            .orElseThrow(() -> MonthlyGoalTypeException.from(MONTHLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findMonthlyGoal.getUserId())) {
            throw new UnAuthorizedException();
        }
        findMonthlyGoal.updateStatus(command.done());
    }
}
