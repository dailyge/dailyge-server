package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.dailyge.app.core.coupon.application.CouponUseCase;
import project.dailyge.app.core.coupon.application.WinnerAlgorithm;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService implements CouponUseCase {

    private final CouponEventValidator validator;
    private final WinnerAlgorithm winnerAlgorithm;
    private final CouponCacheReadRepository couponCacheReadRepository;
    private final CouponCacheWriteRepository couponCacheWriteRepository;

    @Override
    public void findWinners(
        final int winnerCount,
        final Long eventId
    ) {
        validator.validateEventRun(eventId);
        couponCacheWriteRepository.saveEventRun(eventId);
        final int totalCount = couponCacheReadRepository.findQueueCount(eventId);
        if (totalCount == 0) {
            return;
        }
        executeSelection(totalCount, winnerCount, eventId);
        couponCacheWriteRepository.deleteAllBulks(eventId);
    }

    private void executeSelection(
        final int totalCount,
        final int winnerCount,
        final Long eventId
    ) {
        int queueNumber = 0;
        while (queueNumber < totalCount) {
            List<CouponCache> couponEvents = couponCacheReadRepository.findBulks(queueNumber, winnerCount, eventId);
            if (couponEvents.isEmpty()) {
                queueNumber++;
                continue;
            }
            winnerAlgorithm.addEvents(couponEvents, winnerCount);
            queueNumber++;
        }
        //TODO: 쿠폰 발급 과정 처리
        final List<Long> userIds = winnerAlgorithm.selectWinners();
        log.info("winner count: {}", userIds.size());
    }
}
