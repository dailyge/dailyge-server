package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponEventReadRepository {
    boolean existsByUserId(Long userId);

    List<CouponEvent> findBulks(int count);

    List<CouponEvent> findBulksByLimit(int queueNumber, int limit);

    Integer findQueueCount();

    boolean hasSelectionRun();
}
