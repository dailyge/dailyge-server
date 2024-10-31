package project.dailyge.app.core.monthlygoal.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_ID;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalStatusUpdateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalUpdateCommand;
import static project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;
import project.dailyge.entity.goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.goal.MonthlyGoalEntityWriteRepository;
import project.dailyge.entity.goal.MonthlyGoalJpaEntity;

@ApplicationLayer(value = "MonthlyGoalWriteUseCase")
public class MonthlyGoalWriteUseCase implements MonthlyGoalWriteService {

    private final MonthlyGoalEntityReadRepository monthlyGoalReadRepository;
    private final MonthlyGoalEntityWriteRepository monthlyGoalWriteRepository;

    public MonthlyGoalWriteUseCase(
        final MonthlyGoalEntityReadRepository monthlyGoalReadRepository,
        final MonthlyGoalEntityWriteRepository monthlyGoalWriteRepository
    ) {
        this.monthlyGoalReadRepository = monthlyGoalReadRepository;
        this.monthlyGoalWriteRepository = monthlyGoalWriteRepository;
    }

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
            throw CommonException.from(INVALID_USER_ID);
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
            throw CommonException.from(INVALID_USER_ID);
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
            throw CommonException.from(INVALID_USER_ID);
        }
        findMonthlyGoal.updateStatus(command.done());
    }
}
