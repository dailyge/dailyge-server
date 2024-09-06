package project.dailyge.app.core.coupon.persistence;

public interface CouponEventParticipantRepository {
    void save(CouponEventParticipant couponEventParticipant);

    int count();

    void deleteAll();
}
