package project.dailyge.entity.coupon;

import java.util.List;

public interface FreeCouponReadRepository {
    List<FreeCouponJpaEntity> findAllByEventIdAndLimit(Long eventId, int limit);
}
