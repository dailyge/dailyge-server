package project.dailyge.app.core.weeklygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static java.util.Optional.ofNullable;
import org.springframework.stereotype.Repository;
import project.dailyge.app.paging.Cursor;
import static project.dailyge.entity.goal.QWeeklyGoalJpaEntity.weeklyGoalJpaEntity;
import project.dailyge.entity.goal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WeeklyGoalReadDao implements WeeklyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public WeeklyGoalReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<WeeklyGoalJpaEntity> findById(final Long weeklyGoalId) {
        return ofNullable(
            queryFactory.selectFrom(weeklyGoalJpaEntity)
                .where(
                    weeklyGoalJpaEntity.id.eq(weeklyGoalId)
                        .and(weeklyGoalJpaEntity._deleted.eq(false))
                ).fetchOne()
        );
    }

    @Override
    public List<WeeklyGoalJpaEntity> findByUserIdAndWeekStartDateByLimit(
        final Long userId,
        final LocalDateTime weekStartDate,
        final int limit
    ) {
        return queryFactory.selectFrom(weeklyGoalJpaEntity)
            .where(
                weeklyGoalJpaEntity.userId.eq(userId)
                    .and(weeklyGoalJpaEntity.weekStartDate.eq(weekStartDate))
                    .and(weeklyGoalJpaEntity._deleted.eq(false))
            )
            .orderBy(weeklyGoalJpaEntity._createdAt.asc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<WeeklyGoalJpaEntity> findByCursor(
        final Long userId,
        final long index,
        final int limit,
        final LocalDateTime weekStartDate
    ) {
        return queryFactory.selectFrom(weeklyGoalJpaEntity)
            .where(
                weeklyGoalJpaEntity.id.gt(index)
                    .and(weeklyGoalJpaEntity.weekStartDate.eq(weekStartDate))
                    .and(weeklyGoalJpaEntity._deleted.eq(false))
            )
            .orderBy(weeklyGoalJpaEntity._createdAt.asc())
            .limit(limit)
            .fetch();
    }

    public List<WeeklyGoalJpaEntity> findByCursor(
        final Long userId,
        final Cursor cursor,
        final LocalDateTime weekStartDate
    ) {
        if (cursor.isNull()) {
            return findByUserIdAndWeekStartDateByLimit(userId, weekStartDate, cursor.getLimit());
        }
        return findByCursor(userId, cursor.getIndex(), cursor.getLimit(), weekStartDate);
    }
}
