package project.dailyge.app.core.retrospect.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import project.dailyge.app.core.retrospect.persistence.RetrospectEntityReadDao;
import project.dailyge.app.page.CustomPageable;
import project.dailyge.app.response.AsyncPagingResponse;
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
    public AsyncPagingResponse<RetrospectJpaEntity> findRetrospectAndTotalCountByPage(
        final DailygeUser dailygeUser,
        final CustomPageable page
    ) {
        return retrospectEntityReadDao.findRetrospectAndTotalCountByPage(dailygeUser.getId(), page);
    }
}
