package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponEventWriteRepository {
    void saveBulks(final List<CouponEvent> couponCaches);

    void deleteAllBulks();

    void increaseSelectionRunCount();
}
