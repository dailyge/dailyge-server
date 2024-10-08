package project.dailyge.app.core.coupon.application;

import java.util.List;

public interface FreeCouponWriteUseCase {
    void saveAll(List<Long> userIds, Long eventId);
}
