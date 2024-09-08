package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import java.util.List;

@ApplicationLayer
@RequiredArgsConstructor
class CouponEventWriteService implements CouponCacheWriteUseCase {
    private final CouponCacheWriteRepository couponCacheWriteRepository;
    private final CouponInMemoryRepository couponInMemoryRepository;

    @Override
    public void saveBulks() {
        final List<CouponEventParticipant> participants = couponInMemoryRepository.popAll();
        if (participants.isEmpty()) {
            return;
        }
        final List<CouponCache> couponCaches = participants.stream()
            .map(participant -> new CouponCache(participant.getUserId(), participant.getTimestamp()))
            .toList();
        couponCacheWriteRepository.saveBulks(couponCaches);
    }
}
