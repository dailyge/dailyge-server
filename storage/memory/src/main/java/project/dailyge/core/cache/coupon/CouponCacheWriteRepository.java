package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponCacheWriteRepository {

    void saveBulks(List<CouponCache> couponCaches, Long eventId);

    void deleteAllBulks(Long eventId);

    void saveEventRun(Long eventId);
}
