package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.presentation.response.CouponParticipationResponse;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;

@RequiredArgsConstructor
@RequestMapping(path = "/api")
@PresentationLayer(value = "CouponReadApi")
public class CouponReadApi {

    private final CouponCacheReadUseCase couponCacheReadUseCase;

    @GetMapping(path = "/coupons")
    public ApiResponse<CouponParticipationResponse> findCouponParticipationStatus(@LoginUser final DailygeUser dailygeUser) {
        final boolean isParticipated = couponCacheReadUseCase.existsByUserId(dailygeUser.getUserId());
        return ApiResponse.from(OK, new CouponParticipationResponse(isParticipated));
    }
}
