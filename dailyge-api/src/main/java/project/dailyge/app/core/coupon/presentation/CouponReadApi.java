package project.dailyge.app.core.coupon.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.presentation.response.CouponParticipationResponse;
import project.dailyge.core.cache.coupon.CouponEventReadService;

@RequestMapping(path = "/api")
@PresentationLayer(value = "CouponReadApi")
public class CouponReadApi {

    private final CouponEventReadService couponEventReadService;

    public CouponReadApi(final CouponEventReadService couponEventReadService) {
        this.couponEventReadService = couponEventReadService;
    }

    @GetMapping(path = "/coupons")
    public ApiResponse<CouponParticipationResponse> findCouponParticipationStatus(@LoginUser final DailygeUser dailygeUser) {
        final boolean isParticipated = couponEventReadService.existsByUserId(dailygeUser.getUserId());
        return ApiResponse.from(OK, new CouponParticipationResponse(isParticipated));
    }
}
