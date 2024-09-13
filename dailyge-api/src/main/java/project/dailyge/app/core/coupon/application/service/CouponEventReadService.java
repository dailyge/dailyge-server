package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventReadUseCase;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponEventReadService")
class CouponEventReadService implements CouponEventReadUseCase {

    private final CouponEventReadRepository couponEventReadRepository;

    @Override
    public boolean existsByUserId(final Long userId) {
        return couponEventReadRepository.existsByUserId(userId);
    }
}
