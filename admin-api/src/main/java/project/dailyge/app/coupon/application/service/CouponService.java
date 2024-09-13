package project.dailyge.app.coupon.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.coupon.application.CouponUseCase;
import project.dailyge.app.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;

import java.util.List;
import java.util.stream.IntStream;

import static project.dailyge.app.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

@Service
@RequiredArgsConstructor
class CouponService implements CouponUseCase {
    private final CouponCacheWriteRepository couponCacheWriteRepository;
    private final CouponCacheReadRepository couponCacheReadRepository;

    @Override
    public List<Long> findWinners(final int winnerCount, final Long couponCategoryId) {
        if (couponCacheReadRepository.hasSelectionRun()) {
            throw CouponTypeException.from(DUPLICATED_WINNER_SELECTION);
        }
        couponCacheWriteRepository.increaseSelectionRunCount();
        final List<Long> winnerIds = selectWinners(winnerCount);
        couponCacheWriteRepository.deleteAllBulks();
        return winnerIds;
    }

    private List<Long> selectWinners(final int winnerCount) {
        final int queueCount = couponCacheReadRepository.findQueueCount();
        final List<List<CouponCache>> sortedQueues = IntStream.rangeClosed(1, queueCount)
            .mapToObj(couponCacheReadRepository::findBulks)
            .toList();
        final List<Long> userIds = WinnerSelectAlgorithm.mergeSortedQueues(sortedQueues, winnerCount);
        return userIds;
    }

}
