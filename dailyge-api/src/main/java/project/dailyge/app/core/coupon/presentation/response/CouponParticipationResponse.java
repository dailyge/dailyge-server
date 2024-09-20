package project.dailyge.app.core.coupon.presentation.response;

public record CouponParticipationResponse(boolean isParticipated) {

    @Override
    public String toString() {
        return String.format("{\"isParticipated\":\"%s\"}", isParticipated);
    }
}
