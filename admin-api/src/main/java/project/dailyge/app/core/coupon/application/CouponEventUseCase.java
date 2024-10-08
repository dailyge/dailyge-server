package project.dailyge.app.core.coupon.application;

import java.util.List;

public interface CouponEventUseCase {

    List<Long> pickWinners(int count, Long eventId);
}
