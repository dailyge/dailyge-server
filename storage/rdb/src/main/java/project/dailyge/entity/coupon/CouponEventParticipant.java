package project.dailyge.entity.coupon;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CouponEventParticipant {
	private Long userId;
	private LocalDateTime dateTime;

	private CouponEventParticipant() {
	}

	public CouponEventParticipant(Long userId, LocalDateTime dateTime) {
		this.userId = userId;
		this.dateTime = dateTime;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof CouponEventParticipant that)) {
			return false;
		}
		if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
			return false;
		}
		return getDateTime() != null ? getDateTime().equals(that.getDateTime()) : that.getDateTime() == null;
	}

	@Override
	public int hashCode() {
		int result = getUserId() != null ? getUserId().hashCode() : 0;
		result = 31 * result + (getDateTime() != null ? getDateTime().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return String.format("{\"userId\":\"%s\",\"role\":\"%s\"}", userId, dateTime);
	}
}
