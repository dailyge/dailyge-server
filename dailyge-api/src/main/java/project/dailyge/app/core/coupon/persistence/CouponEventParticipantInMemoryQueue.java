package project.dailyge.app.core.coupon.persistence;

import org.springframework.stereotype.Component;
import project.dailyge.document.common.UuidGenerator;

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

    /**
     * 요소 전체 삭제 : 테스트 진행 시 사용되는 메소드
     */
    @Override
    public void deleteAll() {
        queue.clear();
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return queue.contains(new CouponEventParticipant(userId, UuidGenerator.createTimeStamp()));
    }
}
