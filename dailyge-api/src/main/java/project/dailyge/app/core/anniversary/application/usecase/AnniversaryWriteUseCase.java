package project.dailyge.app.core.anniversary.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.application.command.AnniversaryUpdateCommand;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.DUPLICATED_ANNIVERSARY;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.anniversary.AnniversaryEntityReadRepository;
import project.dailyge.entity.anniversary.AnniversaryEntityWriteRepository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

@ApplicationLayer(value = "AnniversaryWriteUseCase")
class AnniversaryWriteUseCase implements AnniversaryWriteService {

    private final AnniversaryEntityReadRepository anniversaryEntityReadRepository;
    private final AnniversaryEntityWriteRepository anniversaryEntityWriteRepository;

    public AnniversaryWriteUseCase(
        final AnniversaryEntityReadRepository anniversaryEntityReadRepository,
        final AnniversaryEntityWriteRepository anniversaryEntityWriteRepository
    ) {
        this.anniversaryEntityReadRepository = anniversaryEntityReadRepository;
        this.anniversaryEntityWriteRepository = anniversaryEntityWriteRepository;
    }

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

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long anniversaryId,
        final AnniversaryUpdateCommand command
    ) {
        final AnniversaryJpaEntity findAnniversary = anniversaryEntityReadRepository.findById(anniversaryId)
            .orElseThrow(() -> AnniversaryTypeException.from(ANNIVERSARY_NOT_FOUND));
        if (!dailygeUser.isValid(findAnniversary.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findAnniversary.update(command.name(), command.date(), command.remind(), command.emojiId());
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long anniversaryId
    ) {
        final AnniversaryJpaEntity findAnniversary = anniversaryEntityReadRepository.findById(anniversaryId)
            .orElseThrow(() -> AnniversaryTypeException.from(ANNIVERSARY_NOT_FOUND));
        if (!dailygeUser.isValid(findAnniversary.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findAnniversary.delete();
    }
}
