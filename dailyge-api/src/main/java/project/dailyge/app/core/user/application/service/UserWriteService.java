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

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.DUPLICATED_EMAIL;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserWriteService implements UserWriteUseCase {

    private final UserEntityReadRepository userReadRepository;
    private final UserEntityWriteRepository userWriteRepository;

    @Override
    @Transactional
    public UserJpaEntity save(final UserJpaEntity user) {
        final Optional<UserJpaEntity> findUser = userReadRepository.findActiveUserByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw UserTypeException.from(DUPLICATED_EMAIL);
        }
        return userWriteRepository.save(user);
    }

    @Override
    @Transactional
    public UserJpaEntity upsert(final UserJpaEntity newUser) {
        final Optional<UserJpaEntity> findUser = userReadRepository.findActiveUserByEmail(newUser.getEmail());
        if (findUser.isEmpty()) {
            return userWriteRepository.save(newUser);
        }
        final UserJpaEntity user = findUser.get();
        if (newUser.getProfileImageUrl() != null) {
            user.profileImageInit(newUser.getProfileImageUrl());
        }
        return userWriteRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(final Long userId) {
        final UserJpaEntity findUser = userReadRepository.findActiveUserById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
        findUser.delete();
        userWriteRepository.save(findUser);
    }
}
