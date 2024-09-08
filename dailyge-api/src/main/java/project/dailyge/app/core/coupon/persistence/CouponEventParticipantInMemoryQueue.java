package project.dailyge.app.core.coupon.persistence;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component
public class CouponEventParticipantInMemoryQueue implements CouponEventParticipantRepository {

    private final BlockingQueue<CouponEventParticipant> queue;

    public CouponEventParticipantInMemoryQueue(final BlockingQueue<CouponEventParticipant> queue) {
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

    /**
     * 큐에 있는 요소들 전체를 List 자료구조에 저장
     */
    @Override
    public List<CouponEventParticipant> popAll() {
        final List<CouponEventParticipant> participants = new ArrayList<>();
        queue.drainTo(participants);
        return participants;
    }
}
