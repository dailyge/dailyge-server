package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.user.application.UserDeleteUseCase;
import project.dailyge.domain.user.UserJpaEntity;
import project.dailyge.domain.user.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserDeleteService implements UserDeleteUseCase {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public void delete(final UserJpaEntity user) {
        userJpaRepository.delete(user);
    }
}
