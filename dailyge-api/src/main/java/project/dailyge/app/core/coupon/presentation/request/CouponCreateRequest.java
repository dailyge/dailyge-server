package project.dailyge.app.core.coupon.presentation.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest(
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime dateTime
) {
	@Override
	public String toString() {
		return String.format("{\"dateTime\":\"%s\"}", dateTime);
	}
}
