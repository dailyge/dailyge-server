package project.dailyge.app.test.coupon.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.core.coupon.presentation.validator.CouponClientValidator;

@DisplayName("[UnitTest] CouponClientValidator 단위 테스트")
class CouponClientValidatorUnitTest {
    private CouponClientValidator couponClientValidator;

    @BeforeEach
    void setUp() {
        couponClientValidator = new CouponClientValidator();
    }

    @Test
    @DisplayName("쿠키 값이 true면 CouponClientValidator는 true를 반환한다.")
    void whenCookieValueIsTrueThenCouponClientValidatorReturnTrue() {
        boolean isParticipated = couponClientValidator.validateParticipant("true");
        assertTrue(isParticipated);
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"invalid", "false", "dailyge", ""})
    @DisplayName("쿠키 값이 true가 아니거나 null이면 CouponClientValidator는 false를 반환한다.")
    void whenCookieValueIsNotTrueOrNullThenCouponClientValidatorReturnFalse(final String cookieValue) {
        boolean isParticipated = couponClientValidator.validateParticipant(cookieValue);
        assertFalse(isParticipated);
    }
}
