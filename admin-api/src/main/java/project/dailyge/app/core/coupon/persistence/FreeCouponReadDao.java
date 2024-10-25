package project.dailyge.app.core.coupon.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponReadRepository;

import java.util.List;

import static project.dailyge.entity.coupon.QFreeCouponJpaEntity.freeCouponJpaEntity;

@Repository
@RequiredArgsConstructor
public class FreeCouponReadDao implements FreeCouponReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FreeCouponJpaEntity> findAllByEventIdAndLimit(
        final Long eventId,
        final int limit
    ) {
        return queryFactory.selectFrom(freeCouponJpaEntity)
            .where(
                freeCouponJpaEntity.eventId.eq(eventId)
            )
            .orderBy(freeCouponJpaEntity.createdAt.asc())
            .limit(limit)
            .fetch();
    }
}
