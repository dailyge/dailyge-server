package project.dailyge.app.core.anniversary.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.anniversary.AnniversaryEntityReadRepository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@ApplicationLayer(value = "AnniversaryReadUseCase")
class AnniversaryReadUseCase implements AnniversaryReadService {

    private final AnniversaryEntityReadRepository anniversaryEntityReadRepository;

    @Override
    public AnniversaryJpaEntity findById(final Long anniversaryId) {
        return anniversaryEntityReadRepository.findById(anniversaryId)
            .orElseThrow(() -> AnniversaryTypeException.from(ANNIVERSARY_NOT_FOUND));
    }

    @Override
    public List<AnniversaryJpaEntity> findByDates(
        final DailygeUser dailygeUser,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        return anniversaryEntityReadRepository.findByDates(dailygeUser.getId(), startDate, endDate);
    }
}
