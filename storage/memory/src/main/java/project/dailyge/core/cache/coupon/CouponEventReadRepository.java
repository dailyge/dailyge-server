package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponEventReadRepository {

    boolean existsByUserId(Long userId, Long eventId);

    List<CouponEvent> findBulks(int queueIndex, int limit, Long eventId);

    Integer findQueueCount(Long eventId);

    boolean isExecuted(Long eventId);
}
