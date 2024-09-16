package project.dailyge.app.coupon.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.coupon.application.CouponUseCase;
import project.dailyge.app.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static project.dailyge.app.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

@Service
@RequiredArgsConstructor
class CouponService implements CouponUseCase {
    private final CouponEventWriteRepository couponEventWriteRepository;
    private final CouponEventReadRepository couponEventReadRepository;

    @Override
    public List<Long> findWinners(
        final int winnerCount,
        final Long couponCategoryId
    ) {
        if (couponEventReadRepository.hasSelectionRun()) {
            throw CouponTypeException.from(DUPLICATED_WINNER_SELECTION);
        }
        couponEventWriteRepository.increaseSelectionRunCount();
        final List<Long> winnerIds = selectWinners(winnerCount);
        if (!winnerIds.isEmpty()) {
            couponEventWriteRepository.deleteAllBulks();
        }
        return winnerIds;
    }

    private List<Long> selectWinners(final int winnerCount) {
        final int queueCount = couponEventReadRepository.findQueueCount();
        if (queueCount == 0) {
            return Collections.emptyList();
        }
        final List<Queue<CouponEvent>> sortedQueues = IntStream.rangeClosed(1, queueCount)
            .mapToObj(couponEventReadRepository::findBulks)
            .toList();
        return KWayMergeAlgorithm.selectWinners(sortedQueues, winnerCount);
    }
}
