package project.dailyge.core.cache.coupon;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class CouponCache implements Serializable, Comparable<CouponCache> {
    private Long userId;
    private Long timestamp;

    private CouponCache() {
    }

    public CouponCache(
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
        if (!(object instanceof CouponCache that)) {
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
    public int compareTo(final CouponCache other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}
