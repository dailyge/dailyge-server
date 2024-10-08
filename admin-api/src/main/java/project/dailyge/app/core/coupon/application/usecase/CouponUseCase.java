package project.dailyge.app.core.coupon.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<<< HEAD:admin-api/src/main/java/project/dailyge/app/core/coupon/application/usecase/CouponUseCase.java
import org.springframework.stereotype.Service;
import project.dailyge.app.core.coupon.application.CouponWriteService;
========
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.coupon.application.CouponEventUseCase;
>>>>>>>> dev:admin-api/src/main/java/project/dailyge/app/core/coupon/application/usecase/CouponEventEventService.java
import project.dailyge.app.core.coupon.application.WinnerAlgorithm;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@ApplicationLayer(value = "CouponService")
@RequiredArgsConstructor
<<<<<<<< HEAD:admin-api/src/main/java/project/dailyge/app/core/coupon/application/usecase/CouponUseCase.java
public class CouponUseCase implements CouponWriteService {
========
class CouponEventEventService implements CouponEventUseCase {
>>>>>>>> dev:admin-api/src/main/java/project/dailyge/app/core/coupon/application/usecase/CouponEventEventService.java

    private final CouponEventValidator validator;
    private final WinnerAlgorithm winnerAlgorithm;
    private final CouponEventReadRepository couponEventReadRepository;
    private final CouponEventWriteRepository couponEventWriteRepository;

    @Override
    public List<Long> pickWinners(
        final int winnerCount,
        final Long eventId
    ) {
        validator.validateEventRun(eventId);
        couponEventWriteRepository.saveEventRun(eventId);
        final int totalCount = couponEventReadRepository.findQueueCount(eventId);
        if (totalCount == 0) {
            return Collections.emptyList();
        }
        final List<Long> userIds = executeSelection(totalCount, winnerCount, eventId);
        couponEventWriteRepository.deleteAllBulks(eventId);
        return userIds;
    }

    private List<Long> executeSelection(
        final int totalCount,
        final int winnerCount,
        final Long eventId
    ) {
        int queueNumber = 1;
        while (queueNumber <= totalCount) {
            final List<CouponEvent> couponEvents = couponEventReadRepository.findBulks(queueNumber, winnerCount, eventId);
            if (couponEvents.isEmpty()) {
                queueNumber++;
                continue;
            }
            winnerAlgorithm.addEvents(couponEvents, winnerCount);
            queueNumber++;
        }
        return winnerAlgorithm.selectWinners();
    }
}
