package project.dailyge.core.cache.coupon;

import java.util.Queue;

public interface CouponEventReadRepository {
    boolean existsByUserId(Long userId);

    Queue<CouponEvent> findBulks(int count);

    Integer findQueueCount();

    boolean hasSelectionRun();
}
