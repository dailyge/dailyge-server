package project.dailyge.app.core.coupon.persistence;

public class CouponEventParticipant {

    private Long userId;
    private Long timestamp;

    private CouponEventParticipant() {
    }

    public CouponEventParticipant(
        final Long userId,
        final Long timestamp
    ) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CouponEventParticipant that)) {
            return false;
        }
        return getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return getUserId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("{\"userId\":\"%s\",\"timestamp\":\"%s\"}", userId, timestamp);
    }
}
