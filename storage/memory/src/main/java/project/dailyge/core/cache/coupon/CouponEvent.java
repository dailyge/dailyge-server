package project.dailyge.core.cache.coupon;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class CouponEvent implements Serializable {
    private Long userId;
    private Long timestamp;

    private CouponEvent() {
    }

    public CouponEvent(
        final Long userId,
        final Long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CouponEvent that)) {
            return false;
        }
        if (!Objects.equals(userId, that.userId)) {
            return false;
        }
        return Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    public boolean isFaster(final CouponEvent other) {
        return other.timestamp > this.timestamp;
    }

    @Override
    public String toString() {
        return String.format("{\"userId\":\"%s\",\"timestamp\":\"%s\"}", userId, timestamp);
    }
}
