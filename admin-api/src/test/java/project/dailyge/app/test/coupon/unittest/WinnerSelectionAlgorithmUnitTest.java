package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.coupon.application.service.WinnerSelectAlgorithm;
import project.dailyge.app.test.coupon.fixture.CouponFixture;
import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("[UnitTest] WinnerSelectionAlgorithm 단위 테스트")
public class WinnerSelectionAlgorithmUnitTest {

    @Test
    @DisplayName("각 리스트들이 비어 있으면 빈 리스트를 반환한다.")
    void whenEventQueuesEmptyThenResultEmpty() {
        final List<Queue<CouponEvent>> emptyList = List.of(new ArrayDeque<>(), new ArrayDeque<>(), new ArrayDeque<>());
        final List<Long> result = WinnerSelectAlgorithm.mergeSortedQueues(emptyList, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("CouponCache 리스트가 한개만 있는 경우 가장 작은 timestamp 들을 가진 userId를 순서대로 반환한다.")
    void whenSingleEventQueueThenResultReturnSortedMinimumElements() {
        final List<Long> expectedResult = LongStream.rangeClosed(1, 1000)
            .boxed()
            .toList();
        final List<Long> targetNumberList = LongStream.rangeClosed(1, 10000)
            .boxed()
            .toList();
        final Queue<CouponEvent> couponCaches = targetNumberList.stream().map(number -> new CouponEvent(number, number))
            .collect(Collectors.toCollection(ArrayDeque::new));
        final List<Long> actualResult = WinnerSelectAlgorithm.mergeSortedQueues(List.of(couponCaches), 1000);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("요소들이 충분히 있으면 가장 작은 요소를 가진 userId 순서대로 반환한다.")
    void whenEventQueuesExistsThenResultReturnSortedMinimumElements() {
        final List<Queue<CouponEvent>> couponCaches = CouponFixture.createCouponEventLists();
        final List<Long> expectedResult = LongStream.rangeClosed(1, 1000)
            .boxed()
            .toList();
        final List<Long> actualResult = WinnerSelectAlgorithm.mergeSortedQueues(couponCaches, 1000);
        assertEquals(expectedResult, actualResult);
    }

    private List<Queue<CouponEvent>> createCouponCaches() {
        final List<Queue<CouponEvent>> couponCacheLists = new ArrayList<>();
        final int numberOfLists = 10;
        for (int idx = 0; idx < numberOfLists; idx++) {
            couponCacheLists.add(new ArrayDeque<>());
        }
        final int elementsPerList = 1000;
        final int limit = 1000;
        final int remained = elementsPerList * numberOfLists - limit;
        for (long timestamp = 1; timestamp <= limit; timestamp++) {
            final int order = (int) timestamp % numberOfLists;
            couponCacheLists.get(order).add(new CouponEvent(timestamp, timestamp));
        }
        for (long timestamp = limit + 10000; timestamp <= limit + 10000 + remained; timestamp++) {
            final int order = (int) timestamp % numberOfLists;
            couponCacheLists.get(order).add(new CouponEvent(timestamp, timestamp));
        }
        return couponCacheLists;
    }
}
