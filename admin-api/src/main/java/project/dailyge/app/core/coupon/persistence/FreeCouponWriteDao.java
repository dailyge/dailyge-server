package project.dailyge.app.core.coupon.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponWriteRepository;

@Repository
@RequiredArgsConstructor
public class FreeCouponWriteDao implements FreeCouponWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final FreeCouponJpaEntity coupon) {
        entityManager.persist(coupon);
        return coupon.getId();
    }
}
