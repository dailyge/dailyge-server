package project.dailyge.app.coupon.application.service;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class WinnerSelectAlgorithm {
    static class CouponNode implements Comparable<CouponNode> {
        private final CouponEvent couponEvent;
        private final int queueOrder;

        private CouponNode(final CouponEvent couponEvent,
                           final int queueOrder
        ) {
            this.couponEvent = couponEvent;
            this.queueOrder = queueOrder;
        }

        @Override
        public int compareTo(final CouponNode other) {
            return this.couponEvent.compareTo(other.couponEvent);
        }
    }

    public static List<Long> mergeSortedQueues(
        final List<Queue<CouponEvent>> sortedCouponQueues,
        final int limit
    ) {
        final PriorityQueue<CouponNode> minHeap = new PriorityQueue<>();
        addAllFirstElements(sortedCouponQueues, minHeap);
        return findSmallestElements(sortedCouponQueues, limit, minHeap);
    }

    private static List<Long> findSmallestElements(
        final List<Queue<CouponEvent>> couponEvents,
        final int limit,
        final PriorityQueue<CouponNode> minHeap
    ) {
        final List<Long> mergedResult = new ArrayList<>();
        while (mergedResult.size() < limit && !minHeap.isEmpty()) {
            final CouponNode topNode = minHeap.poll();
            mergedResult.add(topNode.couponEvent.getUserId());
            final int currentQueueOrder = topNode.queueOrder;
            final Queue<CouponEvent> currentQueue = couponEvents.get(currentQueueOrder);
            if (!currentQueue.isEmpty()) {
                final CouponEvent nextCoupon = currentQueue.poll();
                minHeap.offer(new CouponNode(nextCoupon, currentQueueOrder));
            }
        }
        return mergedResult;
    }

    private static void addAllFirstElements(List<Queue<CouponEvent>> couponQueues, PriorityQueue<CouponNode> minHeap) {
        for (int queueOrder = 0; queueOrder < couponQueues.size(); queueOrder++) {
            final Queue<CouponEvent> currentQueue = couponQueues.get(queueOrder);
            if (!currentQueue.isEmpty()) {
                final CouponEvent firstCoupon = currentQueue.poll();
                minHeap.offer(new CouponNode(firstCoupon, queueOrder));
            }
        }
    }
}
