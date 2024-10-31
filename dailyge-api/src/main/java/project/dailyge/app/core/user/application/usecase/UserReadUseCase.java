package project.dailyge.app.core.user.application.usecase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_ID;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserReadService;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.entity.user.UserEntityReadRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@ApplicationLayer(value = "UserReadUseCase")
class UserReadUseCase implements UserReadService {

    private final UserEntityReadRepository userReadRepository;

    public UserReadUseCase(final UserEntityReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public UserJpaEntity findById(final Long userId) {
        return userReadRepository.findById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
    }

    @Override
    public UserJpaEntity findActiveUserById(final Long userId) {
        return userReadRepository.findActiveUserById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
    }

    @Override
    public UserJpaEntity findAuthorizedUserById(final Long userId) {
        return userReadRepository.findActiveUserById(userId)
            .orElseThrow(() -> CommonException.from(USER_NOT_FOUND.message(), INVALID_USER_ID));
    }

    @Override
    public Optional<UserJpaEntity> findActiveUserByEmail(final String email) {
        return userReadRepository.findActiveUserByEmail(email);
    }

    @Override
    public UserJpaEntity findByNickname(final String nickname) {
        return userReadRepository.findByNickname(nickname)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
    }

    @Override
    public Long findUserIdByEmail(final String email) {
        return userReadRepository.findUserIdByEmail(email);
    }

    @Override
    public String findEmailByUserId(final Long userId) {
        return userReadRepository.findEmailById(userId);
    }

    @Override
    public boolean existsById(final Long userId) {
        final Optional<UserJpaEntity> user = userReadRepository.findActiveUserById(userId);
        return user.isPresent();
    }
}
