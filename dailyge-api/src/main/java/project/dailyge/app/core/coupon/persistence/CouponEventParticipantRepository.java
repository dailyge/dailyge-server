package project.dailyge.app.core.coupon.persistence;

public interface CouponEventParticipantRepository {
    void save(final CouponEventParticipant couponEventParticipant);

    int count();

    void deleteAll();

    boolean existsByUserId(final Long userId);
}
