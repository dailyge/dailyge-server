package project.dailyge.core.cache.coupon;

public interface CouponCacheReadUseCase {
    boolean existsByUserId(final Long userId);
}
