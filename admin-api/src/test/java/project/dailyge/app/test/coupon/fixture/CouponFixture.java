package project.dailyge.app.test.coupon.fixture;

import project.dailyge.core.cache.coupon.CouponCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public final class CouponFixture {
    private CouponFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static List<List<CouponCache>> createCouponEventLists(final int numberOfLists, final int elementsPerList, final int limit) {
        List<List<CouponCache>> couponCacheLists = new ArrayList<>();
        for (int idx = 0; idx < numberOfLists; idx++) {
            couponCacheLists.add(new ArrayList<>());
        }
        final int remained = elementsPerList * numberOfLists - limit;
        for (long timestamp = 1; timestamp <= limit; timestamp++) {
            final int order = (int) timestamp % numberOfLists;
            couponCacheLists.get(order).add(new CouponCache(timestamp, timestamp));
        }
        final int buffer = 10 * limit;
        for (long timestamp = limit + buffer; timestamp <= limit + buffer + remained; timestamp++) {
            final int order = (int) timestamp % numberOfLists;
            couponCacheLists.get(order).add(new CouponCache(timestamp, timestamp));
        }
        return couponCacheLists;
    }

    public static List<List<CouponCache>> createCouponEventLists() {
        return createCouponEventLists(10, 1000, 1000);
    }

    public static List<Long> findExpectedUserIdsList() {
        final List<Long> expectedResult = LongStream.rangeClosed(1, 1000)
            .boxed()
            .toList();
        return expectedResult;
    }
}
