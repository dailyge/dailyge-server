package project.dailyge.entity.test.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] FreeCoupon 엔티티 테스트")
class FreeCouponUnitTest {

    @Test
    @DisplayName("ID가 같다면 같은 객체로 여긴다.")
    void whenIdIsSameThenInstanceAreSameObj() {
        final FreeCouponJpaEntity expectedCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        final FreeCouponJpaEntity newCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        assertThat(newCoupon).isEqualTo(expectedCoupon);
    }

    @Test
    @DisplayName("ID가 다르다면 다른 객체로 여긴다.")
    void whenIdIsNotSameThenInstanceAreNotSameObj() {
        final FreeCouponJpaEntity expectedCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        final FreeCouponJpaEntity newCoupon = new FreeCouponJpaEntity(
            2L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        assertThat(newCoupon).isNotEqualTo(expectedCoupon);
    }

    @Test
    @DisplayName("ID가 같다면 해시코드가 동일하다")
    void whenIdIsSameThenHashCodeAreSame() {
        final FreeCouponJpaEntity expectedCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        final FreeCouponJpaEntity newCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        assertEquals(expectedCoupon.hashCode(), newCoupon.hashCode());
    }

    @Test
    @DisplayName("ID가 다르다면 해시코드가 다르다")
    void whenIdIsNotSameThenHashCodeAreSame() {
        final FreeCouponJpaEntity expectedCoupon = new FreeCouponJpaEntity(
            1L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        final FreeCouponJpaEntity newCoupon = new FreeCouponJpaEntity(
            2L,
            "coupon description",
            UUID.randomUUID().toString(),
            LocalDate.now().plusYears(1),
            1L,
            1L,
            1L
        );

        assertNotEquals(expectedCoupon.hashCode(), newCoupon.hashCode());
    }
}
