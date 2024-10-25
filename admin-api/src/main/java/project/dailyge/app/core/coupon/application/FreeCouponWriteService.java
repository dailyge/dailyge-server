package project.dailyge.app.core.coupon.application;

import java.util.List;

public interface FreeCouponWriteService {
    void saveAll(List<Long> userIds, Long eventId);
}
