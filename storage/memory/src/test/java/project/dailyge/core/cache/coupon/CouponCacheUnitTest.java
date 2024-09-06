package project.dailyge.core.cache.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[UnitTest] UserCache 단위 테스트")
class CouponCacheUnitTest {
    private CouponCache couponCache;
    private Long userId;
    private Long timeStamp;

    @BeforeEach
    void setUp() {
        userId = 1L;
        timeStamp = currentTimeMillis();
        couponCache = new CouponCache(userId, timeStamp);
    }

    @Test
    @DisplayName("Coupon Cache를 생성하면 정상적으로 Cache가 생성된다.")
    void whenCreateCouponCacheThenAllValuesReturnValidValue() {
        assertAll(
            () -> assertEquals(userId, couponCache.getUserId()),
            () -> assertEquals(timeStamp, couponCache.getTimestamp())
        );
    }

    @Test
    @DisplayName("userId와 timestamp가 동일하다면 동일한 객체이다.")
    void whenUserIdAndTimeStampSameThenInstanceSame() {
        final CouponCache copiedCouponCache = new CouponCache(couponCache.getUserId(), couponCache.getTimestamp());
        assertEquals(copiedCouponCache, couponCache);
    }

    @Test
    @DisplayName("userId와 timestamp가 동일하다면 동일한 해시코드를 가진다.")
    void whenUserIdAndTimeStampSameThenHashCodeSame() {
        final CouponCache copiedCouponCache = new CouponCache(couponCache.getUserId(), couponCache.getTimestamp());
        assertEquals(copiedCouponCache.hashCode(), couponCache.hashCode());
    }

    @Test
    @DisplayName("userId가 다르면 값이 다른 객체이다.")
    void whenUserIdDifferentThenInstanceDifferent() {
        final CouponCache copiedCouponCache = new CouponCache(couponCache.getUserId() + 1, couponCache.getTimestamp());
        assertNotEquals(copiedCouponCache, couponCache);
    }

    @Test
    @DisplayName("userId가 다르면 다른 해시코드가 다르다.")
    void whenUserIdDifferentThenHashCodeDifferent() {
        final CouponCache copiedCouponCache = new CouponCache(couponCache.getUserId() + 1, couponCache.getTimestamp());
        assertNotEquals(copiedCouponCache.hashCode(), couponCache.hashCode());
    }
}
