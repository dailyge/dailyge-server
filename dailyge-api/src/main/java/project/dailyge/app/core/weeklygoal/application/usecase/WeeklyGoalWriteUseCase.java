package project.dailyge.app.core.weeklygoal.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;
import static project.dailyge.app.core.weeklygoal.exception.WeeklyGoalCodeAndMessage.WEEKLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.weeklygoal.exception.WeeklyGoalTypeException;
import project.dailyge.entity.goal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.goal.WeeklyGoalEntityWriteRepository;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

@ApplicationLayer(value = "WeeklyGoalWriteUseCase")
class WeeklyGoalWriteUseCase implements WeeklyGoalWriteService {

    private final WeeklyGoalEntityWriteRepository weeklyGoalWriteRepository;
    private final WeeklyGoalEntityReadRepository weeklyGoalReadRepository;

    public WeeklyGoalWriteUseCase(
        final WeeklyGoalEntityWriteRepository weeklyGoalWriteRepository,
        final WeeklyGoalEntityReadRepository weeklyGoalReadRepository
    ) {
        this.weeklyGoalWriteRepository = weeklyGoalWriteRepository;
        this.weeklyGoalReadRepository = weeklyGoalReadRepository;
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final WeeklyGoalCreateCommand command
    ) {
        final WeeklyGoalJpaEntity newWeeklyGoal = command.toEntity(dailygeUser);
        return weeklyGoalWriteRepository.save(newWeeklyGoal);
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long weeklyGoalId,
        final WeeklyGoalUpdateCommand command
    ) {
        final WeeklyGoalJpaEntity findWeeklyGoal = weeklyGoalReadRepository.findById(weeklyGoalId)
            .orElseThrow(() -> WeeklyGoalTypeException.from(WEEKLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findWeeklyGoal.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findWeeklyGoal.update(command.title(), command.content());
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long weeklyGoalId,
        final boolean done
    ) {
        final WeeklyGoalJpaEntity findWeeklyGoal = weeklyGoalReadRepository.findById(weeklyGoalId)
            .orElseThrow(() -> WeeklyGoalTypeException.from(WEEKLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findWeeklyGoal.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findWeeklyGoal.updateDone(done);
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long weeklyGoalId
    ) {
        final WeeklyGoalJpaEntity findWeeklyGoal = weeklyGoalReadRepository.findById(weeklyGoalId)
            .orElseThrow(() -> WeeklyGoalTypeException.from(WEEKLY_GOAL_NOT_FOUND));
        if (!dailygeUser.isValid(findWeeklyGoal.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findWeeklyGoal.delete();
    }
}
