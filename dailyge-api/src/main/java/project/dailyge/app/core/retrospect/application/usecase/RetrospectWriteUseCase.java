package project.dailyge.app.core.retrospect.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.core.retrospect.application.command.RetrospectUpdateCommand;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_NOT_FOUND;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectEntityWriteRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@ApplicationLayer("RetrospectWriteUseCase")
class RetrospectWriteUseCase implements RetrospectWriteService {

    private final RetrospectEntityWriteRepository retrospectEntityWriteRepository;
    private final RetrospectEntityReadRepository retrospectEntityReadRepository;

    public RetrospectWriteUseCase(
        final RetrospectEntityWriteRepository retrospectEntityWriteRepository,
        final RetrospectEntityReadRepository retrospectEntityReadRepository
    ) {
        this.retrospectEntityWriteRepository = retrospectEntityWriteRepository;
        this.retrospectEntityReadRepository = retrospectEntityReadRepository;
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final RetrospectCreateCommand command
    ) {
        final RetrospectJpaEntity newRetrospect = command.toEntity(dailygeUser);
        return retrospectEntityWriteRepository.save(newRetrospect);
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final RetrospectUpdateCommand command,
        final Long retrospectId
    ) {
        final RetrospectJpaEntity findRetrospect = retrospectEntityReadRepository.findById(retrospectId)
            .orElseThrow(() -> RetrospectTypeException.from(RETROSPECT_NOT_FOUND));
        if (!dailygeUser.isValid(findRetrospect.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findRetrospect.update(command.title(), command.content(), command.date(), command.isPublic());
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long retrospectId
    ) {
        final RetrospectJpaEntity findRetrospect = retrospectEntityReadRepository.findById(retrospectId)
            .orElseThrow(() -> RetrospectTypeException.from(RETROSPECT_NOT_FOUND));
        if (!dailygeUser.isValid(findRetrospect.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findRetrospect.delete();
    }
}
