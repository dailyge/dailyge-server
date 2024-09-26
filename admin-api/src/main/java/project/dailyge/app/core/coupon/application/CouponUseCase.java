package project.dailyge.app.core.coupon.application;

public interface CouponUseCase {

    void findWinners(int count, Long eventId);
}
