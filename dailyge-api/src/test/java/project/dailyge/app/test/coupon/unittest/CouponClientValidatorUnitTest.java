package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.core.coupon.presentation.validator.CouponClientValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[UnitTest] CouponClientValidator 단위 테스트")
class CouponClientValidatorUnitTest {
    private CouponClientValidator couponClientValidator;

    @BeforeEach
    void setUp() {
        couponClientValidator = new CouponClientValidator();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("쿠키 값이 없으며 CouponClientValidator는 false를 반환한다.")
    void whenCookieValueIsNullThenCouponClientValidatorReturnFalse(final String cookieValue) {
        boolean isParticipated = couponClientValidator.validateParticipant(cookieValue);
        assertFalse(isParticipated);
    }

    @Test
    @DisplayName("쿠키 값이 true면 CouponClientValidator는 true를 반환한다.")
    void whenCookieValueIsTrueThenCouponClientValidatorReturnTrue() {
        boolean isParticipated = couponClientValidator.validateParticipant("true");
        assertTrue(isParticipated);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "false", "dailyge"})
    @DisplayName("쿠키 값이 true와 다르면 CouponClientValidator는 true를 반환한다.")
    void whenCookieValueIsNotTrueThenCouponClientValidatorReturnFalse(final String cookieValue) {
        boolean isParticipated = couponClientValidator.validateParticipant(cookieValue);
        assertFalse(isParticipated);
    }

}
