package project.dailyge.app.core.monthlygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static java.util.Optional.ofNullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;
import static project.dailyge.entity.monthlygoal.QMonthlyGoalJpaEntity.monthlyGoalJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MonthlyGoalReadDao implements MonthlyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MonthlyGoalJpaEntity> findById(final Long monthlyGoalId) {
        return ofNullable(
            queryFactory.selectFrom(monthlyGoalJpaEntity)
                .where(
                    monthlyGoalJpaEntity.id.eq(monthlyGoalId)
                        .and(monthlyGoalJpaEntity.deleted.eq(false))
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
                    .and(monthlyGoalJpaEntity.deleted.eq(false))
            )
            .orderBy(monthlyGoalJpaEntity.createdAt.asc())
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
                    .and(monthlyGoalJpaEntity.deleted.eq(false))
            )
            .limit(cursor.getLimit())
            .fetch();
    }
}
