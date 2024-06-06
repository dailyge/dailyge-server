package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserEntityReadRepository;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_USER_ID;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_FOUND_MESSAGE;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.ACTIVE_USER_NOT_FOUND;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
class UserReadService implements UserReadUseCase {

    private final UserEntityReadRepository readRepository;

    @Override
    public UserJpaEntity findById(final Long userId) {
        return readRepository.findById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
    }

    @Override
    public UserJpaEntity findActiveUserById(final Long userId) {
        return readRepository.findActiveUserById(userId)
            .orElseThrow(() -> UserTypeException.from(ACTIVE_USER_NOT_FOUND));
    }

    @Override
    public UserJpaEntity findAuthorizedById(final Long userId) {
        return readRepository.findActiveUserById(userId)
            .orElseThrow(() -> new UnAuthorizedException(USER_NOT_FOUND_MESSAGE, INVALID_USER_ID));
    }

    @Override
    public Optional<UserJpaEntity> findActiveUserByEmail(final String email) {
        return readRepository.findActiveUserByEmail(email);
    }
}
