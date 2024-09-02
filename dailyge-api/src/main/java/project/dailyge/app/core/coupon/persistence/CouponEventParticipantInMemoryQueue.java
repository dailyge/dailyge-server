package project.dailyge.app.core.coupon.persistence;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CouponEventParticipantInMemoryQueue implements CouponEventParticipantRepository {
    private final BlockingQueue<CouponEventParticipant> queue;

    public CouponEventParticipantInMemoryQueue(BlockingQueue<CouponEventParticipant> queue) {
        this.queue = queue;
    }

    @Override
    public void save(final CouponEventParticipant couponEventParticipant) {
        queue.add(couponEventParticipant);
    }

    @Override
    public int count() {
        return queue.size();
    }

    @Override
    public void deleteAll() {
        queue.clear();
    }
}
