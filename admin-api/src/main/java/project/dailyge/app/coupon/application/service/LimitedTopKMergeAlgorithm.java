package project.dailyge.app.coupon.application.service;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class LimitedTopKMergeAlgorithm {
    public static List<Long> selectWinners(final List<Queue<CouponEvent>> sortedCouponQueues, final int limit) {
        final PriorityQueue<CouponEvent> queue = new PriorityQueue<>(2 * limit,
            (x, y) -> Long.compare(y.getTimestamp(), x.getTimestamp()));
        for (Queue<CouponEvent> couponEvents : sortedCouponQueues) {
            for (int count = 0; count < limit; count++) {
                queue.add(couponEvents.poll());
            }
            final int exceedCount = queue.size() - limit;
            for (int count = 0; count < exceedCount; count++) {
                queue.poll();
            }
        }
        return queue.stream().map(CouponEvent::getUserId).toList();
    }
}
