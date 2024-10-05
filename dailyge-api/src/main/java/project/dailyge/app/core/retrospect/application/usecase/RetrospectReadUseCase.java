package project.dailyge.app.core.retrospect.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_NOT_FOUND;

@RequiredArgsConstructor
@ApplicationLayer(value = "RetrospectReadUseCase")
public class RetrospectReadUseCase implements RetrospectReadService {

    private final RetrospectEntityReadRepository retrospectEntityReadRepository;

    @Override
    public RetrospectJpaEntity findById(final Long retrospectId) {
        return retrospectEntityReadRepository.findById(retrospectId)
            .orElseThrow(() -> RetrospectTypeException.from(RETROSPECT_NOT_FOUND));
    }
}
