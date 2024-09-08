package project.dailyge.app.core.coupon.persistence;

import java.util.List;

public interface CouponEventParticipantRepository {
    void save(CouponEventParticipant couponEventParticipant);

    int count();

    void deleteAll();

    List<CouponEventParticipant> popAll();
}
