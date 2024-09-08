package project.dailyge.app.core.coupon.persistence;

import project.dailyge.core.cache.coupon.CouponCache;

import java.util.List;

public record CouponEventBulks(List<CouponCache> couponCaches) {
}
