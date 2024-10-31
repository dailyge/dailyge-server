package project.dailyge.app.core.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponReadRepository;
import static project.dailyge.entity.coupon.QFreeCouponJpaEntity.freeCouponJpaEntity;

import java.util.List;

@Repository
public class FreeCouponReadDao implements FreeCouponReadRepository {

    private final JPAQueryFactory queryFactory;

    public FreeCouponReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<FreeCouponJpaEntity> findAllByEventIdAndLimit(
        final Long eventId,
        final int limit
    ) {
        return queryFactory.selectFrom(freeCouponJpaEntity)
            .where(
                freeCouponJpaEntity.eventId.eq(eventId)
            )
            .orderBy(freeCouponJpaEntity._createdAt.asc())
            .limit(limit)
            .fetch();
    }
}
