package project.dailyge.core.cache.coupon;

import java.util.List;

public interface CouponEventWriteRepository {

    void saveBulks(List<CouponEvent> couponCaches, Long eventId);

    void deleteAllBulks(Long eventId);

    void saveEventRun(Long eventId);
}
