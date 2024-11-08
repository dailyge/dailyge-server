package project.dailyge.app.core.monthlygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static java.util.Optional.ofNullable;
import org.springframework.stereotype.Repository;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.goal.MonthlyGoalJpaEntity;
import static project.dailyge.entity.goal.QMonthlyGoalJpaEntity.monthlyGoalJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class MonthlyGoalReadDao implements MonthlyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public MonthlyGoalReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<MonthlyGoalJpaEntity> findById(final Long monthlyGoalId) {
        return ofNullable(
            queryFactory.selectFrom(monthlyGoalJpaEntity)
                .where(
                    monthlyGoalJpaEntity.id.eq(monthlyGoalId)
                        .and(monthlyGoalJpaEntity._deleted.eq(false))
                ).fetchOne()
        );
    }

    @Override
    public List<MonthlyGoalJpaEntity> findByUserIdAndYearAndMonth(
        final Long userId,
        final int year,
        final int month
    ) {
        return queryFactory.selectFrom(monthlyGoalJpaEntity)
            .where(
                monthlyGoalJpaEntity.userId.eq(userId)
                    .and(monthlyGoalJpaEntity.year.eq(year))
                    .and(monthlyGoalJpaEntity.month.eq(month))
                    .and(monthlyGoalJpaEntity._deleted.eq(false))
            )
            .orderBy(monthlyGoalJpaEntity._createdAt.asc())
            .limit(10)
            .fetch();
    }

    public List<MonthlyGoalJpaEntity> findMonthlyGoalsByCursor(
        final Long userId,
        final Cursor cursor
    ) {
        return queryFactory.selectFrom(monthlyGoalJpaEntity)
            .where(
                monthlyGoalJpaEntity.id.gt(cursor.getIndex())
                    .and(monthlyGoalJpaEntity.userId.eq(userId))
                    .and(monthlyGoalJpaEntity._deleted.eq(false))
            )
            .limit(cursor.getLimit())
            .fetch();
    }
}
