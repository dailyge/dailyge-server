package project.dailyge.app.core.coupon.presentation.response;

import lombok.Getter;

@Getter
public class CouponParticipationResponse {

    private boolean isParticipated;

    private CouponParticipationResponse() {
    }

    public CouponParticipationResponse(boolean isParticipated) {
        this.isParticipated = isParticipated;
    }
}
