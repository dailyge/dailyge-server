package project.dailyge.app.core.weeklygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

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
}
