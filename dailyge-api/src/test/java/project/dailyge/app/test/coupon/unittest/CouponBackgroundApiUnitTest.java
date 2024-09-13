package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.coupon.presentation.CouponBackgroundApi;
import project.dailyge.core.cache.coupon.CouponEventWriteUseCase;

import static org.mockito.Mockito.*;

@DisplayName("[UnitTest] 쿠폰 백그라운드 작업 처리 API 테스트")
class CouponBackgroundApiUnitTest {
    private CouponEventWriteUseCase couponEventWriteUseCase;
    private CouponBackgroundApi couponBackgroundApi;

    @BeforeEach
    void setUp() {
        couponEventWriteUseCase = mock(CouponEventWriteUseCase.class);
        couponBackgroundApi = new CouponBackgroundApi(couponEventWriteUseCase);
    }

    @Test
    @DisplayName("쿠폰 벌크 처리를 요청하면 쿠폰 벌크 작업이 처리된다.")
    void whenRequestCouponBulksThenProcessCouponBulksBackground() {
        couponBackgroundApi.processBulkRequests();
        verify(couponEventWriteUseCase, times(1)).saveBulks();
    }
}
