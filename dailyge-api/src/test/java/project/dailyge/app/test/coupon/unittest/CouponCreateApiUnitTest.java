package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteService;
import project.dailyge.app.core.coupon.presentation.CouponCreateApi;
import project.dailyge.entity.user.Role;

@DisplayName("[UnitTest] 쿠폰 발급 신청 API 테스트")
class CouponCreateApiUnitTest {

    private CouponWriteService couponWriteService;
    private CouponCreateApi couponCreateApi;
    private String env;

    @BeforeEach
    void setUp() {
        couponWriteService = mock(CouponWriteService.class);
        env = "dev";
        couponCreateApi = new CouponCreateApi(env, couponWriteService);
    }

    @Test
    @DisplayName("쿠폰 발급 신청을 하면 쿠폰 발급 신청 사용자 정보가 저장된다.")
    void whenCreateCouponApplyThenSaveCouponApply() {
        final DailygeUser user = new DailygeUser(1L, Role.NORMAL);
        couponCreateApi.createCouponApply(user);
        verify(couponWriteService, times(1)).saveApply(user);
    }
}
