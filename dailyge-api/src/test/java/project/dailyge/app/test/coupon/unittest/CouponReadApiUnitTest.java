package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.presentation.CouponReadApi;
import project.dailyge.core.cache.coupon.CouponEventReadUseCase;
import project.dailyge.entity.user.Role;

import static org.mockito.Mockito.*;

@DisplayName("[UnitTest] 쿠폰 발급 참여 확인 API 테스트")
class CouponReadApiUnitTest {

    private CouponEventReadUseCase couponEventReadUseCase;
    private CouponReadApi couponReadApi;

    @BeforeEach
    void setUp() {
        couponEventReadUseCase = mock(CouponEventReadUseCase.class);
        couponReadApi = new CouponReadApi(couponEventReadUseCase);
    }

    @Test
    @DisplayName("쿠폰관련 쿠키가 없으면 CouponCacheReadUseCase 를 호출한다.")
    void whenCookieValueDoesNotExistThenCallCouponCacheReadUseCase() {
        DailygeUser user = new DailygeUser(1L, Role.NORMAL);
        couponReadApi.findCouponParticipationStatus(user);
        verify(couponEventReadUseCase, times(1)).existsByUserId(user.getUserId());
    }
}
