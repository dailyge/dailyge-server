package project.dailyge.core.cache.coupon;

public interface CouponEventReadUseCase {
    boolean existsByUserId(final Long userId);
}
