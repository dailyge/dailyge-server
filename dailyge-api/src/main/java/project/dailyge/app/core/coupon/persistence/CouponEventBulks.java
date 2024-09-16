package project.dailyge.app.core.coupon.persistence;

import project.dailyge.core.cache.coupon.CouponEvent;

import java.util.Queue;

public record CouponEventBulks(Queue<CouponEvent> couponCaches) {
}
