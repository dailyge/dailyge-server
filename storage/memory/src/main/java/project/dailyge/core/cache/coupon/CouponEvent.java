package project.dailyge.core.cache.coupon;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class CouponEvent implements Serializable, Comparable<CouponEvent> {
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
    public boolean equals(final Object object) {
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


    @Override
    public int compareTo(final CouponEvent other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}
