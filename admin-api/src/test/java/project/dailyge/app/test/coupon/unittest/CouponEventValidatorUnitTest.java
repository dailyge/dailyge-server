package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.coupon.application.usecase.CouponEventValidator;
import project.dailyge.app.core.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

@DisplayName("[UnitTest] CouponEventValidator 단위테스트")
class CouponEventValidatorUnitTest {

    private CouponEventReadRepository couponEventReadRepository;
    private CouponEventValidator couponEventValidator;

    @BeforeEach
    void setUp() {
        couponEventReadRepository = mock(CouponEventReadRepository.class);
        couponEventValidator = new CouponEventValidator(couponEventReadRepository);
    }

    @Test
    @DisplayName("당첨자 선정 작업이 이미 실행되었으면 CouponTypeException을 던진다.")
    void whenSelectionOccursThenCouponTypeExceptionShouldBeHappen() {
        final Long eventId = 1L;
        when(couponEventReadRepository.isExecuted(eventId)).thenReturn(true);
        assertThrows(CouponTypeException.from(DUPLICATED_WINNER_SELECTION).getClass(),
            () -> couponEventValidator.validateEventRun(eventId));
    }

    @Test
    @DisplayName("당첨자 선정 작업이 실행되지 않았으면 아무런 예외도 던지지 않는다.")
    void whenSelectionDoesNotOccursThenCouponTypeExceptionShouldNotBeHappen() {
        final Long eventId = 1L;
        when(couponEventReadRepository.isExecuted(eventId)).thenReturn(false);
        assertDoesNotThrow(() -> couponEventReadRepository.isExecuted(1L));
    }
}
