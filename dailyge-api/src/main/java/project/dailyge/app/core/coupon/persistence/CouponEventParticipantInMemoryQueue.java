package project.dailyge.app.core.coupon.persistence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import project.dailyge.entity.coupon.CouponEventParticipant;
import project.dailyge.entity.coupon.CouponEventParticipantRepository;

@Component
public class CouponEventParticipantInMemoryQueue implements CouponEventParticipantRepository {
	private final BlockingQueue<CouponEventParticipant> queue;
	public CouponEventParticipantInMemoryQueue() {
		this.queue = new LinkedBlockingQueue<>();
	}

	@Override
	public void save(final CouponEventParticipant couponEventParticipant) {
		queue.add(couponEventParticipant);
	}
}
