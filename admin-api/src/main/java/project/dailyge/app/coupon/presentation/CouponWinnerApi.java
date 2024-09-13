package project.dailyge.app.coupon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.coupon.application.CouponUseCase;
import project.dailyge.app.coupon.presentation.request.CouponWinnerRequest;
import project.dailyge.app.coupon.presentation.response.CouponWinnerResponse;

import java.util.List;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponWinnerApi {

    private final CouponUseCase couponUseCase;

    @PostMapping(path = "/coupons/winners")
    public ApiResponse<CouponWinnerResponse> findWinners(@Valid @RequestBody final CouponWinnerRequest couponWinnerRequest) {
        final List<Long> winners = couponUseCase.findWinners(couponWinnerRequest.winnerCount(), couponWinnerRequest.couponCategoryId());
        final CouponWinnerResponse payload = new CouponWinnerResponse(winners);
        return ApiResponse.from(OK, payload);
    }
}
