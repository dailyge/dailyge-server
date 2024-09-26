package project.dailyge.app.core.coupon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.application.CouponUseCase;
import project.dailyge.app.core.coupon.presentation.request.CouponWinnerRequest;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponWinnerApi {

    private final CouponUseCase couponUseCase;

    @PostMapping(path = "/winners")
    public ApiResponse<Void> findWinners(@Valid @RequestBody final CouponWinnerRequest request) {
        couponUseCase.findWinners(request.winnerCount(), request.eventId());
        return ApiResponse.from(OK);
    }
}
