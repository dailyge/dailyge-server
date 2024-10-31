package project.dailyge.app.core.coupon.application.usecase;

import jakarta.transaction.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.application.FreeCouponWriteService;
import project.dailyge.document.common.UuidGenerator;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponWriteRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationLayer(value = "CouponWriteService")
class CouponWriteUseCase implements FreeCouponWriteService {

    private final FreeCouponWriteRepository freeCouponWriteRepository;

    public CouponWriteUseCase(final FreeCouponWriteRepository freeCouponWriteRepository) {
        this.freeCouponWriteRepository = freeCouponWriteRepository;
    }

    @Override
    @Transactional
    public void saveAll(
        final List<Long> userIds,
        final Long eventId
    ) {
        userIds.forEach(userId -> {
            final FreeCouponJpaEntity coupon = new FreeCouponJpaEntity(
                "300코인 할인 이벤트",
                UuidGenerator.createTimeBasedUUID(),
                LocalDate.now().plusYears(1),
                1L,
                userId,
                eventId
            );
            freeCouponWriteRepository.save(coupon);
        });
    }
}
