package project.dailyge.app.core.coupon.persistence;

import lombok.Getter;

@Getter
public class CouponEventParticipant {
    private Long userId;
    private Long timestamp;

    private CouponEventParticipant() {
    }

    public CouponEventParticipant(Long userId, Long timestamp) {
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
        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
            return false;
        }
        return getTimestamp() != null ? getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() == null;
    }

    @Override
    public int hashCode() {
        int result = getUserId() != null ? getUserId().hashCode() : 0;
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("{\"userId\":\"%s\",\"timestamp\":\"%s\"}", userId, timestamp);
    }
}
