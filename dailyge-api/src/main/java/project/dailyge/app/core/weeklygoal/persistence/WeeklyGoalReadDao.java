package project.dailyge.app.core.weeklygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static project.dailyge.entity.weeklygoal.QWeeklyGoalJpaEntity.weeklyGoalJpaEntity;

@Repository
@RequiredArgsConstructor
class WeeklyGoalReadDao implements WeeklyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<WeeklyGoalJpaEntity> findById(final Long weeklyGoalId) {
        return ofNullable(
            queryFactory.selectFrom(weeklyGoalJpaEntity)
                .where(
                    weeklyGoalJpaEntity.id.eq(weeklyGoalId)
                        .and(weeklyGoalJpaEntity.deleted.eq(false))
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
                    .and(weeklyGoalJpaEntity.deleted.eq(false))
            )
            .orderBy(weeklyGoalJpaEntity.createdAt.asc())
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
                    .and(weeklyGoalJpaEntity.deleted.eq(false))
            )
            .orderBy(weeklyGoalJpaEntity.createdAt.asc())
            .limit(limit)
            .fetch();
    }
}
