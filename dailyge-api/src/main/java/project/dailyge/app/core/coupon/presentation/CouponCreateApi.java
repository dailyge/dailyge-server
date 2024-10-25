package project.dailyge.app.core.coupon.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteService;

@RequestMapping(path = "/api")
@PresentationLayer(value = "CouponCreateApi")
public class CouponCreateApi {

    private final String env;
    private final CouponWriteService couponWriteService;

    public CouponCreateApi(
        @Value("${env}") final String env,
        final CouponWriteService couponWriteService
    ) {
        this.env = env;
        this.couponWriteService = couponWriteService;
    }

    @PostMapping(path = "/coupons")
    public ApiResponse<Void> createCouponApply(@LoginUser final DailygeUser dailygeUser) {
        couponWriteService.saveApply(dailygeUser);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, createResponseCookie("isParticipated", "true", "/", 7L * 60L * 60L, true, env));
        return ApiResponse.from(CommonCodeAndMessage.CREATED, headers, null);
    }
}
