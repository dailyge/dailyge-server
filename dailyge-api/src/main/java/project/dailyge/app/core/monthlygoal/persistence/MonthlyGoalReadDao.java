package project.dailyge.app.core.monthlygoal.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.monthlygoal.MonthlyGoalEntityReadRepository;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;
import static project.dailyge.entity.monthlygoal.QMonthlyGoalJpaEntity.monthlyGoalJpaEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MonthlyGoalReadDao implements MonthlyGoalEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
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
