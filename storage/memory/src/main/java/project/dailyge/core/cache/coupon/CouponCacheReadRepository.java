package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponCacheReadRepository {
    boolean existsByUserId(Long userId);

    List<CouponCache> findBulks(int count);

    Integer findQueueCount();
}
