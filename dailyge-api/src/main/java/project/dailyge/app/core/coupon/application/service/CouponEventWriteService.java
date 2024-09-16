package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;
import project.dailyge.core.cache.coupon.CouponEventWriteUseCase;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponEventWriteService")
class CouponEventWriteService implements CouponEventWriteUseCase {
    private final CouponEventWriteRepository couponEventWriteRepository;
    private final CouponInMemoryRepository couponInMemoryRepository;

    @Override
    public void saveBulks() {
        final List<CouponEventParticipant> participants = couponInMemoryRepository.popAll();
        if (participants.isEmpty()) {
            return;
        }
        final Queue<CouponEvent> couponCaches = participants.stream()
            .map(participant -> new CouponEvent(participant.getUserId(), participant.getTimestamp()))
            .collect(Collectors.toCollection(ArrayDeque::new));
        couponEventWriteRepository.saveBulks(couponCaches);
    }
}
