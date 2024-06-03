package project.dailyge.app.core.user.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.user.application.UserDeleteUseCase;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.service.UserWriteService;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_EMAIL_CONFLICT;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserReadUseCase userReadUseCase;
    private final UserWriteService userWriteService;
    private final UserDeleteUseCase userDeleteUseCase;

    @Transactional
    public UserJpaEntity save(final UserJpaEntity userJpaEntity) {
        Optional<UserJpaEntity> findUser = userReadUseCase.findByEmail(userJpaEntity.getEmail());
        if (findUser.isPresent()) {
            throw UserTypeException.from(USER_EMAIL_CONFLICT);
        }
        return userWriteService.save(userJpaEntity);
    }

    @Transactional
    public void delete(Long userId) {
        UserJpaEntity findUser = userReadUseCase.findById(userId);
        userDeleteUseCase.delete(findUser);
    }
}
