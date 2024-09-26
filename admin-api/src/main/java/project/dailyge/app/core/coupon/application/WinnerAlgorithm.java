package project.dailyge.app.core.coupon.application;

import org.springframework.stereotype.Component;
import project.dailyge.core.cache.coupon.CouponCache;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Long.compare;

@Component
public class WinnerAlgorithm {

    private final PriorityQueue<CouponCache> priorityQueue = new PriorityQueue<>((x, y) -> compare(y.getTimestamp(), x.getTimestamp()));

    public void addEvents(
        final List<CouponCache> couponEvents,
        final int winnerCount
    ) {
        couponEvents.forEach(event -> addEvent(event, winnerCount));
    }

    private void addEvent(
        final CouponCache couponEvent,
        final int winnerCount
    ) {
        if (priorityQueue.size() < winnerCount) {
            priorityQueue.add(couponEvent);
        } else {
            replaceEvent(couponEvent);
        }
    }

    private void replaceEvent(final CouponCache couponEvent) {
        final CouponCache comparedEvent = priorityQueue.peek();
        if (couponEvent.isFaster(comparedEvent)) {
            priorityQueue.poll();
            priorityQueue.add(couponEvent);
        }
    }

    public List<Long> selectWinners() {
        final List<Long> userIds = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            final CouponCache couponCache = priorityQueue.poll();
            userIds.add(couponCache.getUserId());
        }
        return userIds;
    }
}
