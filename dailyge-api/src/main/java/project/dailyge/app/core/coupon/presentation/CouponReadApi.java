package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.presentation.response.CouponParticipationResponse;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class CouponReadApi {
    private static final String IS_PARTICIPATED = "isParticipated";
    private static final String VALID_COOKIE_VALUE = "true";
    private final CouponCacheReadUseCase couponCacheReadUseCase;

    @GetMapping(path = "/coupons")
    public ApiResponse<CouponParticipationResponse> findCouponParticipationStatus(
        @LoginUser final DailygeUser dailygeUser,
        @CookieValue(value = IS_PARTICIPATED, required = false) final String cookieValue
    ) {
        if (cookieValue != null && cookieValue.equals(VALID_COOKIE_VALUE)) {
            return ApiResponse.from(OK, new CouponParticipationResponse(true));
        }
        final boolean isParticipated = couponCacheReadUseCase.existsByUserId(dailygeUser.getUserId());
        return ApiResponse.from(OK, new CouponParticipationResponse(isParticipated));
    }
}
