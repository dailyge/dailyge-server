package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponCreateApi {
    private final CouponWriteUseCase couponWriteUseCase;

    @PostMapping(path = "/coupons")
    public ApiResponse<Void> createCouponApply(
        @LoginUser final DailygeUser dailygeUser
    ) {
        couponWriteUseCase.saveApply(dailygeUser);
        return ApiResponse.from(CREATED);
    }
}
