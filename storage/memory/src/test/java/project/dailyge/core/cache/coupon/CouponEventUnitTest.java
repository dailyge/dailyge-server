package project.dailyge.core.cache.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] UserCache 단위 테스트")
class CouponEventUnitTest {
    private CouponEvent couponEvent;
    private Long userId;
    private Long timeStamp;

    @BeforeEach
    void setUp() {
        userId = 1L;
        timeStamp = currentTimeMillis();
        couponEvent = new CouponEvent(userId, timeStamp);
    }

    @Test
    @DisplayName("Coupon Cache를 생성하면 정상적으로 Cache가 생성된다.")
    void whenCreateCouponCacheThenAllValuesReturnValidValue() {
        assertAll(
            () -> assertEquals(userId, couponEvent.getUserId()),
            () -> assertEquals(timeStamp, couponEvent.getTimestamp())
        );
    }

    @Test
    @DisplayName("userId와 timestamp가 동일하다면 동일한 객체이다.")
    void whenUserIdAndTimeStampSameThenInstanceSame() {
        final CouponEvent copiedCouponEvent = new CouponEvent(couponEvent.getUserId(), couponEvent.getTimestamp());
        assertEquals(copiedCouponEvent, couponEvent);
    }

    @Test
    @DisplayName("userId와 timestamp가 동일하다면 동일한 해시코드를 가진다.")
    void whenUserIdAndTimeStampSameThenHashCodeSame() {
        final CouponEvent copiedCouponEvent = new CouponEvent(couponEvent.getUserId(), couponEvent.getTimestamp());
        assertEquals(copiedCouponEvent.hashCode(), couponEvent.hashCode());
    }

    @Test
    @DisplayName("userId가 다르면 값이 다른 객체이다.")
    void whenUserIdDifferentThenInstanceDifferent() {
        final CouponEvent copiedCouponEvent = new CouponEvent(couponEvent.getUserId() + 1, couponEvent.getTimestamp());
        assertNotEquals(copiedCouponEvent, couponEvent);
    }

    @Test
    @DisplayName("userId가 다르면 다른 해시코드가 다르다.")
    void whenUserIdDifferentThenHashCodeDifferent() {
        final CouponEvent copiedCouponEvent = new CouponEvent(couponEvent.getUserId() + 1, couponEvent.getTimestamp());
        assertNotEquals(copiedCouponEvent.hashCode(), couponEvent.hashCode());
    }
}
