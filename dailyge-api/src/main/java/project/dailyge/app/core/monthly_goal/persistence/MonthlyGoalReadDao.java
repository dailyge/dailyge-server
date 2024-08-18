package project.dailyge.app.core.monthly_goal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.monthly_goal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthly_goal.MonthlyGoalJpaEntity;
import static project.dailyge.entity.monthly_goal.QMonthlyGoalJpaEntity.monthlyGoalJpaEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MonthlyGoalReadDao implements MonthlyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<MonthlyGoalJpaEntity> findById(final Long monthlyGoalId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(monthlyGoalJpaEntity)
                .where(
                    monthlyGoalJpaEntity.id.eq(monthlyGoalId)
                        .and(monthlyGoalJpaEntity.deleted.eq(false))
                ).fetchOne()
        );
    }
}
