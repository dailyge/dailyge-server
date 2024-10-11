package project.dailyge.app.core.weeklygoal.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalReadService;
import project.dailyge.app.core.weeklygoal.application.validator.WeeklyGoalValidator;
import project.dailyge.app.cursor.Cursor;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "WeeklyGoalReadUseCase")
class WeeklyGoalReadUseCase implements WeeklyGoalReadService {

    private final WeeklyGoalValidator validator;
    private final WeeklyGoalEntityReadRepository weeklyGoalEntityReadRepository;

    @Override
    public List<WeeklyGoalJpaEntity> findPageByCursor(
        final DailygeUser dailygeUser,
        final Cursor cursor,
        final LocalDateTime weekStartDate
    ) {
        validator.validateWeekStartDate(weekStartDate);
        if (cursor.isNull()) {
            return weeklyGoalEntityReadRepository.findByUserIdAndWeekStartDateByLimit(dailygeUser.getUserId(),
                weekStartDate, cursor.getLimit());
        }
        return weeklyGoalEntityReadRepository.findByCursor(dailygeUser.getUserId(), cursor.getIndex(), cursor.getLimit(), weekStartDate);
    }
}
