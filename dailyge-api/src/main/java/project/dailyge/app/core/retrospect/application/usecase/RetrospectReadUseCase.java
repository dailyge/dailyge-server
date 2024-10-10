package project.dailyge.app.core.retrospect.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import project.dailyge.app.core.retrospect.persistence.RetrospectEntityReadDao;
import project.dailyge.app.page.Page;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_NOT_FOUND;

@RequiredArgsConstructor
@ApplicationLayer(value = "RetrospectReadUseCase")
class RetrospectReadUseCase implements RetrospectReadService {

    private final RetrospectEntityReadRepository retrospectEntityReadRepository;
    private final RetrospectEntityReadDao retrospectEntityReadDao;

    @Override
    public RetrospectJpaEntity findById(final Long retrospectId) {
        return retrospectEntityReadRepository.findById(retrospectId)
            .orElseThrow(() -> RetrospectTypeException.from(RETROSPECT_NOT_FOUND));
    }

    @Override
    public List<RetrospectJpaEntity> findRetrospectByPage(
        final DailygeUser dailygeUser,
        final @OffsetPageable Page page
    ) {
        return retrospectEntityReadDao.findRetrospectByPage(dailygeUser.getId(), page);
    }
}
