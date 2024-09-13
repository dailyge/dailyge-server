package project.dailyge.app.coupon.application.service;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class WinnerSelectAlgorithm {
    static class CouponNode implements Comparable<CouponNode> {
        private final CouponEvent couponEvent;
        private final int queueOrder;
        private final int positionInQueue;

        private CouponNode(final CouponEvent couponEvent, final int queueOrder, final int positionInQueue) {
            this.couponEvent = couponEvent;
            this.queueOrder = queueOrder;
            this.positionInQueue = positionInQueue;
        }

        @Override
        public int compareTo(final CouponNode other) {
            return this.couponEvent.compareTo(other.couponEvent);
        }
    }

    public static List<Long> mergeSortedQueues(List<List<CouponEvent>> sortedCouponQueues, int limit) {
        final PriorityQueue<CouponNode> minHeap = new PriorityQueue<>();
        addAllFirstElements(sortedCouponQueues, minHeap);
        return findSmallestElements(sortedCouponQueues, limit, minHeap);
    }

    private static List<Long> findSmallestElements(List<List<CouponEvent>> couponEvents, int limit, PriorityQueue<CouponNode> minHeap) {
        final List<Long> mergedResult = new ArrayList<>();
        while (mergedResult.size() < limit && !minHeap.isEmpty()) {
            final CouponNode topNode = minHeap.poll();
            mergedResult.add(topNode.couponEvent.getUserId());
            final int currentQueueOrder = topNode.queueOrder;
            final int nextPositionInQueue = topNode.positionInQueue + 1;
            final List<CouponEvent> currentQueue = couponEvents.get(currentQueueOrder);
            if (nextPositionInQueue < currentQueue.size()) {
                final CouponEvent nextCoupon = currentQueue.get(nextPositionInQueue);
                minHeap.offer(new CouponNode(nextCoupon, currentQueueOrder, nextPositionInQueue));
            }
        }
        return mergedResult;
    }

    private static void addAllFirstElements(List<List<CouponEvent>> couponEvents, PriorityQueue<CouponNode> minHeap) {
        for (int queueOrder = 0; queueOrder < couponEvents.size(); queueOrder++) {
            final List<CouponEvent> currentQueue = couponEvents.get(queueOrder);
            if (!currentQueue.isEmpty()) {
                final CouponEvent firstCoupon = currentQueue.get(0);
                minHeap.offer(new CouponNode(firstCoupon, queueOrder, 0));
            }
        }
    }
}
