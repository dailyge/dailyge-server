package project.dailyge.app.core.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.presentation.response.CouponParticipationResponse;
import project.dailyge.app.core.coupon.presentation.validator.CouponClientValidator;

import java.io.IOException;

@Component
public class CouponApplyInterceptor implements HandlerInterceptor {

    private final CouponClientValidator couponClientValidator;
    private final ObjectMapper objectMapper;

    public CouponApplyInterceptor(
        final CouponClientValidator couponClientValidator,
        final ObjectMapper objectMapper
    ) {
        this.couponClientValidator = couponClientValidator;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        final Cookies cookies = new Cookies(request.getCookies());
        if (couponClientValidator.isParticipated(cookies)) {
            try {
                setCouponResponse(response);
            } catch (Exception exception) {
                return false;
            }
            return false;
        }
        return true;
    }

    private void setCouponResponse(
        final HttpServletResponse response
    ) throws IOException {
        response.setStatus(SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        final CouponParticipationResponse couponParticipationResponse = new CouponParticipationResponse(true);
        objectMapper.writeValue(response.getWriter(), ApiResponse.from(OK, couponParticipationResponse));
    }
}
