package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.common.web.CouponApplyInterceptor;

import static org.mockito.Mockito.*;

@DisplayName("[UnitTest] CouponApplyInterceptor 단위 테스트")
public class CouponApplyInterceptorUnitTest {
    private static final String IS_PARTICIPATED = "isParticipated";
    private static final int maxAge = 7 * 24 * 60 * 60;
    private CouponApplyInterceptor couponApplyInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        couponApplyInterceptor = new CouponApplyInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("POST 요청을 보내면 쿠기가 Response에 추가된다.")
    void whenSendPostRequestThenPariticipatedCookieisAdded() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        couponApplyInterceptor.preHandle(request, response, null);
        verify(response).addCookie(any(Cookie.class));
    }
}
