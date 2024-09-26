package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponCacheReadRepository {

    boolean existsByUserId(Long userId, Long eventId);

    List<CouponCache> findBulks(int queueIndex, int limit, Long eventId);

    Integer findQueueCount(Long eventId);

    boolean isExecuted(Long eventId);
}
