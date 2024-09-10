package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@ApplicationLayer
@RequiredArgsConstructor
class CouponEventReadService implements CouponCacheReadUseCase {

    private final CouponCacheReadRepository couponCacheReadRepository;
    private final CouponCacheWriteRepository couponCacheWriteRepository;
    private final BlockingQueue<CouponCache> couponWinnerQueue;

    @Override
    public boolean existsByUserId(final Long userId) {
        return couponCacheReadRepository.existsByUserId(userId);
    }

    @Override
    public void findWinners(final int winnerCount) {
        //스레드 진입 막고
        //변수도 설정해주기 만약 이미 실행을 했다면 break;
        final int queueCount = couponCacheReadRepository.findQueueCount();
        for (int queueNumber = 1; queueNumber <= queueCount; queueNumber++) {
            final List<CouponCache> couponCaches = couponCacheReadRepository.findBulks(queueNumber);
            for (int order = 0; order < winnerCount; order++) {
                final CouponCache couponEvent = couponCaches.get(order);
                if (couponWinnerQueue.size() == winnerCount) {
                    final CouponCache topCouponEvent = couponWinnerQueue.peek();
                    if (topCouponEvent.getTimestamp() > couponEvent.getTimestamp()) {
                        couponWinnerQueue.poll();
                        couponWinnerQueue.add(couponEvent);
                    }
                } else {
                    couponWinnerQueue.add(couponEvent);
                }
            }
        }
        couponCacheWriteRepository.deleteAllBulks();
    }
}
