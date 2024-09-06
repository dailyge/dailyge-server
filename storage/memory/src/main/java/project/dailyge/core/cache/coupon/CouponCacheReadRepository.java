package project.dailyge.core.cache.coupon;

public interface CouponCacheReadRepository {
    boolean existsByUserId(Long userId);
}
