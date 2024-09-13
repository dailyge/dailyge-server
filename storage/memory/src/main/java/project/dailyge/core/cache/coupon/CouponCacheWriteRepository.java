package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponCacheWriteRepository {
    void saveBulks(final List<CouponCache> couponCaches);

    void deleteAllBulks();

    void increaseSelectionRunCount();
}
