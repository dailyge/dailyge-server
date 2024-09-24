package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

@ApplicationLayer(value = "SchedulerService")
@RequiredArgsConstructor
class SchedulerService {

    private final CouponCacheWriteUseCase couponCacheWriteUseCase;

    @Scheduled(fixedRate = 5000)
    public void schedule() {
        couponCacheWriteUseCase.saveBulks();
    }
}
