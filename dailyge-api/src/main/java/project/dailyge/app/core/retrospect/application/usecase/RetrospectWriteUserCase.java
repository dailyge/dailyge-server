package project.dailyge.app.core.retrospect.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.entity.retrospect.RetrospectEntityWriteRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@RequiredArgsConstructor
@ApplicationLayer("RetrospectWriteUserCase")
public class RetrospectWriteUserCase implements RetrospectWriteService {

    private final RetrospectEntityWriteRepository retrospectEntityWriteRepository;

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final RetrospectCreateCommand command
    ) {
        final RetrospectJpaEntity newRetrospect = command.toEntity(dailygeUser);
        return retrospectEntityWriteRepository.save(newRetrospect);
    }
}
