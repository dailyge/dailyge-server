package project.dailyge.app.core.coupon.presentation.response;

public class CouponParticipationResponse {

    private boolean isParticipated;

    private CouponParticipationResponse() {
    }

    public CouponParticipationResponse(boolean isParticipated) {
        this.isParticipated = isParticipated;
    }
}
