package project.dailyge.app.coupon.persistence;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.List;

public record CouponEventBulks(List<CouponEvent> couponCaches) {
}
