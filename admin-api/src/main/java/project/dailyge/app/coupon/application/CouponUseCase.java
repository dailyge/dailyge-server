package project.dailyge.app.coupon.application;

import java.util.List;

public interface CouponUseCase {
    List<Long> findWinners(int count, Long couponCategoryId);
}
