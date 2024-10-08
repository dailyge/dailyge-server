package project.dailyge.app.core.coupon.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventReadService;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponEventReadService")
class CouponEventReadUseCase implements CouponEventReadService {

    private final CouponEventReadRepository couponEventReadRepository;

    @Override
    public boolean existsByUserId(final Long userId) {
        return couponEventReadRepository.existsByUserId(userId, 1L);
    }
}
