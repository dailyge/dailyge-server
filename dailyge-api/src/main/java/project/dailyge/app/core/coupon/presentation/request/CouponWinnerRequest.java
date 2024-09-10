package project.dailyge.app.core.coupon.presentation.request;

import jakarta.validation.constraints.NotNull;

public record CouponWinnerRequest(
    @NotNull(message = "당첨자 수를 입력해 주세요.")
    Integer winnerCount
) {

}
