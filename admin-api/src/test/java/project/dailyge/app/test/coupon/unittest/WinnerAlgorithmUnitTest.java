package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.coupon.application.WinnerAlgorithm;
import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] WinnerAlgorithm 단위테스트")
class WinnerAlgorithmUnitTest {
    private WinnerAlgorithm winnerAlgorithm;

    @BeforeEach
    void setUp() {
        winnerAlgorithm = new WinnerAlgorithm();
    }

    @Test
    @DisplayName("동일한 타임스탬프를 가지는 데이터가 있으면 당첨자 아이디 개수는 당첨자 수와 동일해야 한다.")
    void whenUseSameTimeStampDataThenWinnerIdSizeShouldBeSameWithWinnerCount() {
        final int winnerCount = 4;
        final int queueCount = 2;
        List<CouponEvent> couponEvents = makeExpectedCouponEvents(winnerCount);
        for (int i = 0; i < queueCount; i++) {
            winnerAlgorithm.addEvents(couponEvents, winnerCount);
        }
        final List<Long> userIds = winnerAlgorithm.selectWinners();
        assertEquals(winnerCount, userIds.size());
    }

    @Test
    @DisplayName("랜덤 데이터를 넣으면 예상결과와 똑같아야 한다.")
    void whenUseRandomDataThenResultShouldBeSameWithExpectedData() {
        final int winnerCount = 1000;
        final List<CouponEvent> expectedWinCouponEvents = makeExpectedCouponEvents(winnerCount);
        final List<List<CouponEvent>> candidates = makeRandomData(100_000, 200, expectedWinCouponEvents);
        candidates.forEach(events -> winnerAlgorithm.addEvents(events, winnerCount));
        final List<Long> winnerIds = winnerAlgorithm.selectWinners();
        winnerIds.sort(Long::compare);
        final List<Long> expectedWinnerIds = expectedWinCouponEvents.stream()
            .map(CouponEvent::getUserId).toList();
        assertEquals(expectedWinnerIds, winnerIds);
    }

    private List<CouponEvent> makeExpectedCouponEvents(final int winnerCount) {
        return LongStream.range(1L, winnerCount + 1)
            .mapToObj(number -> new CouponEvent(2 * number, number))
            .toList();
    }

    private List<List<CouponEvent>> makeRandomData(
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
                couponEvents.add(new CouponEvent(2 * timestamp, timestamp));
            }
            remainedCount -= queueSize;
            remainedWinner -= winnerSize;
            couponEventsList.add(couponEvents);
        }
        return couponEventsList;
    }
}
