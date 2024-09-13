package project.dailyge.app.coupon.presentation.request;

import jakarta.validation.constraints.NotNull;

public record CouponWinnerRequest(
    @NotNull(message = "당첨자 수를 입력해 주세요.")
    Integer winnerCount,
    @NotNull(message = "쿠폰 종류 id를 입력해 주세요.")
    Long couponCategoryId
) {

}
