package project.dailyge.core.cache.coupon;

import java.util.Queue;

public interface CouponEventWriteRepository {
    void saveBulks(Queue<CouponEvent> couponCaches);

    void deleteAllBulks();

    void increaseSelectionRunCount();
}
