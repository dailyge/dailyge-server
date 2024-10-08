package project.dailyge.app.core.weeklygoal.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityWriteRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

@RequiredArgsConstructor
@ApplicationLayer(value = "WeeklyGoalWriteUseCase")
class WeeklyGoalWriteUseCase implements WeeklyGoalWriteService {

    private final WeeklyGoalEntityWriteRepository weeklyGoalEntityWriteRepository;

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final WeeklyGoalCreateCommand command
    ) {
        final WeeklyGoalJpaEntity newWeeklyGoal = command.toEntity(dailygeUser);
        return weeklyGoalEntityWriteRepository.save(newWeeklyGoal);
    }
}
