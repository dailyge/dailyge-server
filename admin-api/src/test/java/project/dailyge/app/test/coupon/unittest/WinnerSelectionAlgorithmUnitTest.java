package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.dailyge.app.coupon.application.service.KWayMergeAlgorithm;
import project.dailyge.app.coupon.application.service.LimitedTopKMergeAlgorithm;
import project.dailyge.app.test.coupon.fixture.CouponFixture;
import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("[UnitTest] 당첨자 선정 알고리즘 단위 테스트")
public class WinnerSelectionAlgorithmUnitTest {
    private static final Logger log = LoggerFactory.getLogger(WinnerSelectionAlgorithmUnitTest.class);

    @Test
    @DisplayName("각 리스트들이 비어 있으면 빈 리스트를 반환한다.")
    void whenEventQueuesEmptyThenResultEmpty() {
        final List<Queue<CouponEvent>> emptyList = List.of(new ArrayDeque<>(), new ArrayDeque<>(), new ArrayDeque<>());
        final List<Long> result = KWayMergeAlgorithm.selectWinners(emptyList, 5);
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
        final List<Long> actualResult = KWayMergeAlgorithm.selectWinners(List.of(couponCaches), 1000);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("요소들이 충분히 있으면 가장 작은 요소를 가진 userId 순서대로 반환한다.")
    void whenEventQueuesExistsThenResultReturnSortedMinimumElements() {
        final List<Queue<CouponEvent>> couponCaches = CouponFixture.createCouponEventLists();
        final List<Long> expectedResult = LongStream.rangeClosed(1, 1000)
            .boxed()
            .toList();
        final List<Long> actualResult = KWayMergeAlgorithm.selectWinners(couponCaches, 1000);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("K-way merge 알고리즘 성능 테스트")
    void whenKWayMergeThenFindSmallestElements() {
        final int limit = 10000;
        final List<Queue<CouponEvent>> couponCaches = CouponFixture.createCouponEventLists(100, 100_000, limit);
        long startTime = nanoTime();
        final List<Long> actualUserIds = KWayMergeAlgorithm.selectWinners(couponCaches, limit);
        long endTime = nanoTime();
        assertEquals(CouponFixture.findExpectedUserIdList(limit), actualUserIds);
        log.info("K-way Merge 알고리즘 : {}ms", NANOSECONDS.toMillis(endTime - startTime));
    }

    @Test
    @DisplayName("상위 K개 merge 알고리즘 성능 테스트")
    void whenLimitedTopKMergeThenFindSmallestElements() {
        final int limit = 10000;
        final List<Queue<CouponEvent>> couponCaches = CouponFixture.createCouponEventLists(100, 100_000, limit);
        long startTime = nanoTime();
        final List<Long> actualUserIds = LimitedTopKMergeAlgorithm.selectWinners(couponCaches, limit);
        long endTime = nanoTime();
        assertEquals(CouponFixture.findExpectedUserIdList(limit), actualUserIds.stream().sorted().toList());
        log.info(" Merge and Trim 알고리즘 : {}ms", NANOSECONDS.toMillis(endTime - startTime));
    }
}
