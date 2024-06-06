package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserEntityReadRepository;
import project.dailyge.domain.user.UserEntityWriteRepository;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_EMAIL_CONFLICT;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserWriteService implements UserWriteUseCase {

    private final UserEntityReadRepository readRepository;
    private final UserEntityWriteRepository writeRepository;

    @Override
    @Transactional
    public UserJpaEntity save(final UserJpaEntity user) {
        final Optional<UserJpaEntity> findUser = readRepository.findActiveUserByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw UserTypeException.from(USER_EMAIL_CONFLICT);
        }
        return writeRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(final Long userId) {
        final UserJpaEntity findUser = readRepository.findById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
        findUser.delete();
        writeRepository.save(findUser);
    }
}
