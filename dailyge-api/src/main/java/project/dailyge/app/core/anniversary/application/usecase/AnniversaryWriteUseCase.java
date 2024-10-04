package project.dailyge.app.core.anniversary.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.DUPLICATED_ANNIVERSARY;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.entity.anniversary.AnniversaryEntityReadRepository;
import project.dailyge.entity.anniversary.AnniversaryEntityWriteRepository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

@RequiredArgsConstructor
@ApplicationLayer(value = "AnniversaryWriteUseCase")
class AnniversaryWriteUseCase implements AnniversaryWriteService {

    private final AnniversaryEntityReadRepository anniversaryEntityReadRepository;
    private final AnniversaryEntityWriteRepository anniversaryEntityWriteRepository;

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final AnniversaryCreateCommand command
    ) {
        if (anniversaryEntityReadRepository.exists(command.name(), command.date())) {
            throw AnniversaryTypeException.from(DUPLICATED_ANNIVERSARY);
        }
        final AnniversaryJpaEntity newAnniversary = command.toEntity(dailygeUser);
        return anniversaryEntityWriteRepository.save(newAnniversary);
    }
}
