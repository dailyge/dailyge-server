package project.dailyge.app.core.coupon.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;
import project.dailyge.core.cache.coupon.CouponEventWriteService;

import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponEventWriteService")
class CouponEventWriteUseCase implements CouponEventWriteService {

    private final CouponEventWriteRepository couponEventWriteRepository;
    private final CouponInMemoryRepository couponInMemoryRepository;

    @Override
    public void saveBulks() {
        final List<CouponEventParticipant> participants = couponInMemoryRepository.popAll();
        if (participants.isEmpty()) {
            return;
        }
        final List<CouponEvent> couponCaches = participants.stream()
            .map(participant -> new CouponEvent(participant.getUserId(), participant.getTimestamp()))
            .toList();
        couponEventWriteRepository.saveBulks(couponCaches, 1L);
    }
}
