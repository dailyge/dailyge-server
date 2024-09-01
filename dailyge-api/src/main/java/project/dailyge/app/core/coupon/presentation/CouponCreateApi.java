package project.dailyge.app.core.coupon.presentation;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.presentation.request.CouponCreateRequest;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponCreateApi {
	private final CouponWriteUseCase couponWriteUseCase;

	@PostMapping(path = "/coupons")
	public ApiResponse<Void> createCouponApply(
		@LoginUser final DailygeUser dailygeUser,
		@Valid @RequestBody final CouponCreateRequest request
	) {
		couponWriteUseCase.saveApply(dailygeUser, request.dateTime());
		return ApiResponse.from(CREATED);
	}
}
