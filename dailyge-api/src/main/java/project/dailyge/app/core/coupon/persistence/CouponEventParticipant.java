package project.dailyge.app.core.coupon.persistence;

import lombok.Getter;

@Getter
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

    @Override
    public boolean equals(Object object) {
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
