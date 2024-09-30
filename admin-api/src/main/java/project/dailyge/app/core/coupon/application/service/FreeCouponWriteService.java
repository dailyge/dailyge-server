package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.application.FreeCouponWriteUseCase;
import project.dailyge.document.common.UuidGenerator;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponWriteRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "FreeCouponWriteService")
class FreeCouponWriteService implements FreeCouponWriteUseCase {

    private final FreeCouponWriteRepository freeCouponWriteRepository;

    @Override
    @Transactional
    public void saveAll(final List<Long> userIds, final Long eventId) {
        userIds.stream().forEach(userId -> {
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
