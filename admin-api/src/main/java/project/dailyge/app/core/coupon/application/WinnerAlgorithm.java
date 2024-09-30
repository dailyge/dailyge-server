package project.dailyge.app.core.coupon.application;

import org.springframework.stereotype.Component;
import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Long.compare;

@Component
public class WinnerAlgorithm {

    private final PriorityQueue<CouponEvent> priorityQueue = new PriorityQueue<>((x, y) -> compare(y.getTimestamp(), x.getTimestamp()));

    public void addEvents(
        final List<CouponEvent> couponEvents,
        final int winnerCount
    ) {
        couponEvents.forEach(event -> addEvent(event, winnerCount));
    }

    private void addEvent(
        final CouponEvent couponEvent,
        final int winnerCount
    ) {
        if (priorityQueue.size() < winnerCount) {
            priorityQueue.add(couponEvent);
        } else {
            replaceEvent(couponEvent);
        }
    }

    private void replaceEvent(final CouponEvent couponEvent) {
        final CouponEvent comparedEvent = priorityQueue.peek();
        if (couponEvent.isFaster(comparedEvent)) {
            priorityQueue.poll();
            priorityQueue.add(couponEvent);
        }
    }

    public List<Long> selectWinners() {
        final List<Long> userIds = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            final CouponEvent couponEvent = priorityQueue.poll();
            userIds.add(couponEvent.getUserId());
        }
        return userIds;
    }
}
