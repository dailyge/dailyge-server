package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantRepository;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import java.util.List;

@ApplicationLayer
@RequiredArgsConstructor
class CouponCacheWriteService implements CouponCacheWriteUseCase {
    private final CouponCacheWriteRepository couponCacheWriteRepository;
    private final CouponEventParticipantRepository couponEventParticipantRepository;

    @Override
    public void saveBulks() {
        final List<CouponEventParticipant> participants = couponEventParticipantRepository.popAll();
        if (participants.size() == 0) {
            return;
        }
        final List<CouponCache> couponCaches = participants.stream()
            .map(participant -> new CouponCache(participant.getUserId(), participant.getTimestamp()))
            .toList();
        couponCacheWriteRepository.saveBulks(couponCaches);
    }
}
