package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.document.common.UuidGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] CouponParticipant 단위 테스트")
class CouponParticipantUnitTest {
    @Test
    @DisplayName("userId가 같으면 equals 반환값이 true이다.")
    void whenUserIdisSameThenEqualsReturnTrue() {
        CouponEventParticipant participant1 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        CouponEventParticipant participant2 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        assertEquals(true, participant1.equals(participant2));
    }

    @Test
    @DisplayName("userId가 다르면 equals 반환값이 false이다.")
    void whenUserIdisDifferentThenEqualsReturnFalse() {
        CouponEventParticipant participant1 = new CouponEventParticipant(11L, UuidGenerator.createTimeStamp());
        CouponEventParticipant participant2 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        assertEquals(false, participant1.equals(participant2));
    }

    @Test
    @DisplayName("userId가 같으면 hashCode가 동일하다.")
    void whenUserIdisSameThenHashCodeisSame() {
        CouponEventParticipant participant1 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        CouponEventParticipant participant2 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    @DisplayName("userId가 다르면 hashCode가 다르다.")
    void whenUserIdisDifferentThenHashCodeisDifferent() {
        CouponEventParticipant participant1 = new CouponEventParticipant(10L, UuidGenerator.createTimeStamp());
        CouponEventParticipant participant2 = new CouponEventParticipant(11L, UuidGenerator.createTimeStamp());
        assertNotEquals(participant1.hashCode(), participant2.hashCode());
    }
}
