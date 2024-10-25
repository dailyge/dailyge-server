package project.dailyge.app.core.monthlygoal.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadService;
import project.dailyge.app.core.monthlygoal.application.validator.MonthlyGoalValidator;
import static project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;
import project.dailyge.app.core.monthlygoal.persistence.MonthlyGoalReadDao;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "MonthlyGoalReadService")
public class MonthlyGoalReadUseCase implements MonthlyGoalReadService {

    private final MonthlyGoalValidator validator;
    private final MonthlyGoalReadDao monthlyGoalReadDao;
    private final MonthlyGoalEntityReadRepository monthlyGoalReadRepository;

    @Override
    public MonthlyGoalJpaEntity findById(final Long monthlyGoalId) {
        return monthlyGoalReadRepository.findById(monthlyGoalId)
            .orElseThrow(() -> MonthlyGoalTypeException.from(MONTHLY_GOAL_NOT_FOUND));
    }

    @Override
    public List<MonthlyGoalJpaEntity> findMonthlyGoalsByCursor(
        final DailygeUser dailygeUser,
        final Cursor cursor,
        final Integer year,
        final Integer month
    ) {
        if (cursor.isNull()) {
            validator.validateYearAndMonth(year, month);
            return monthlyGoalReadDao.findByUserIdAndYearAndMonth(dailygeUser.getId(), year, month);
        }
        return monthlyGoalReadDao.findMonthlyGoalsByCursor(dailygeUser.getId(), cursor);
    }
}
