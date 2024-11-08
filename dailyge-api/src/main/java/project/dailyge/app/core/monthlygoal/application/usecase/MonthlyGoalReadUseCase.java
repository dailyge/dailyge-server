package project.dailyge.app.core.monthlygoal.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadService;
import project.dailyge.app.core.monthlygoal.application.validator.MonthlyGoalValidator;
import static project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;
import project.dailyge.app.core.monthlygoal.persistence.MonthlyGoalReadDao;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.goal.MonthlyGoalJpaEntity;

import java.util.List;

@ApplicationLayer(value = "MonthlyGoalReadUseCase")
public class MonthlyGoalReadUseCase implements MonthlyGoalReadService {

    private final MonthlyGoalValidator validator;
    private final MonthlyGoalReadDao monthlyGoalReadDao;
    private final MonthlyGoalEntityReadRepository monthlyGoalReadRepository;

    public MonthlyGoalReadUseCase(
        final MonthlyGoalValidator validator,
        final MonthlyGoalReadDao monthlyGoalReadDao,
        final MonthlyGoalEntityReadRepository monthlyGoalReadRepository
    ) {
        this.validator = validator;
        this.monthlyGoalReadDao = monthlyGoalReadDao;
        this.monthlyGoalReadRepository = monthlyGoalReadRepository;
    }

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
