package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponCacheReadRepository {
    boolean existsByUserId(Long userId);

    List<CouponCache> findBulks(int count);

    List<CouponCache> findBulksByLimit(int queueNumber, int limit);

    Integer findQueueCount();

    boolean hasSelectionRun();
}
