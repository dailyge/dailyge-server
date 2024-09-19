package project.dailyge.app.core.coupon.presentation.request;

public record ScheduleRateRequest(int period) {

    @Override
    public String toString() {
        return String.format("{\"period\":\"%s\"}", period);
    }
}
