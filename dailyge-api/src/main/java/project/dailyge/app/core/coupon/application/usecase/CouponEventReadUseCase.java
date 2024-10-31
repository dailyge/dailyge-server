package project.dailyge.app.core.coupon.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventReadService;

@ApplicationLayer(value = "CouponEventReadService")
class CouponEventReadUseCase implements CouponEventReadService {

    private final CouponEventReadRepository couponEventReadRepository;

    public CouponEventReadUseCase(final CouponEventReadRepository couponEventReadRepository) {
        this.couponEventReadRepository = couponEventReadRepository;
    }

    @Override
    public boolean existsByUserId(final Long userId) {
        return couponEventReadRepository.existsByUserId(userId, 1L);
    }
}
