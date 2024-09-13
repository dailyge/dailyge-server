package project.dailyge.app.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import project.dailyge.app.core.common.web.Cookies;
import project.dailyge.app.core.common.web.CouponApplyInterceptor;
import project.dailyge.app.core.coupon.presentation.validator.CouponClientValidator;

@DisplayName("[UnitTest] CouponApplyInterceptor 단위 테스트")
class CouponApplyInterceptorUnitTest {

    private ObjectMapper objectMapper;
    private CouponClientValidator couponClientValidator;
    private CouponApplyInterceptor couponApplyInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        couponClientValidator = mock(CouponClientValidator.class);
        couponApplyInterceptor = new CouponApplyInterceptor(couponClientValidator, objectMapper);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("요청을 보내면 쿠폰 관련 쿠키를 검증한다.")
    void whenSendRequestThenValidateCouponCookie() {
        couponApplyInterceptor.preHandle(request, response, null);
        verify(couponClientValidator).isParticipated(any(Cookies.class));
    }
}
