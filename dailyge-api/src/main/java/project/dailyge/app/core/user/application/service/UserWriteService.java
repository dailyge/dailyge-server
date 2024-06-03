package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.domain.user.UserJpaEntity;
import project.dailyge.domain.user.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserWriteService implements UserWriteUseCase {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public UserJpaEntity save(final UserJpaEntity user) {
        return userJpaRepository.save(user);
    }
}
