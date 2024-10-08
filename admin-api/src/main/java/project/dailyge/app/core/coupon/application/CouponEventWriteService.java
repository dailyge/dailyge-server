package project.dailyge.app.core.coupon.application;

import java.util.List;

public interface CouponEventWriteService {

    List<Long> pickWinners(int count, Long eventId);
}
