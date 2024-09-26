package project.dailyge.app.test.coupon.fixture;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

public class CouponWinnerFixture {

    private CouponWinnerFixture() {
        throw new AssertionError("Invalid constructor");
    }

    public static List<CouponEvent> makeExpectedCouponEvents(final int winnerCount) {
        return LongStream.range(1L, winnerCount + 1)
            .mapToObj(number -> new CouponEvent(2 * number, number))
            .toList();
    }

    public static List<List<CouponEvent>> makeRandomData(
        final int totalCount,
        final int queueCount,
        final List<CouponEvent> winners
    ) {
        final Random random = new Random();
        final List<List<CouponEvent>> couponEventsList = new ArrayList<>();
        int remainedCount = totalCount - winners.size();
        int remainedWinner = winners.size();
        for (int number = 1; number <= queueCount; number++) {
            final List<CouponEvent> couponEvents = new ArrayList<>();
            int queueSize = random.nextInt(remainedCount + 1);
            int winnerSize = random.nextInt(remainedWinner + 1);
            if (number == queueCount) {
                queueSize = remainedCount;
                winnerSize = remainedWinner;
            }
            for (int idx = remainedWinner - 1; idx > remainedWinner - 1 - winnerSize; idx--) {
                couponEvents.add(winners.get(idx));
            }
            for (int idx = 0; idx < queueSize; idx++) {
                final long timestamp = random.nextLong(winners.size() + 1, Long.MAX_VALUE);
                couponEvents.add(new CouponEvent(Long.MAX_VALUE % 100_001 + 1, timestamp));
            }
            remainedCount -= queueSize;
            remainedWinner -= winnerSize;
            couponEvents.sort(Comparator.comparingLong(CouponEvent::getTimestamp));
            couponEventsList.add(couponEvents);
        }
        return couponEventsList;
    }
}
