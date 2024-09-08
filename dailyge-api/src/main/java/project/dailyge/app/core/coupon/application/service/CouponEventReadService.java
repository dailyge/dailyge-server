package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;

@ApplicationLayer
@RequiredArgsConstructor
class CouponEventReadService implements CouponCacheReadUseCase {

    private final CouponCacheReadRepository couponCacheReadRepository;

    @Override
    public boolean existsByUserId(final Long userId) {
        return couponCacheReadRepository.existsByUserId(userId);
    }
}
