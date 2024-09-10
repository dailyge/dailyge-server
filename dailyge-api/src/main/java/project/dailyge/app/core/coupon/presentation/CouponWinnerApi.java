package project.dailyge.app.core.coupon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.presentation.request.CouponWinnerRequest;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponWinnerApi {

    private final CouponCacheReadUseCase couponCacheReadUseCase;

    @PostMapping(path = "/coupons/winners")
    public ApiResponse<Void> findWinners(@Valid @RequestBody final CouponWinnerRequest couponWinnerRequest) {
        couponCacheReadUseCase.findWinners(couponWinnerRequest.winnerCount());
        return ApiResponse.from(OK);
    }
}
