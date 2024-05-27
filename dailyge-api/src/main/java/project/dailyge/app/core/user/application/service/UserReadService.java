package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.domain.user.User;
import project.dailyge.domain.user.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserReadService implements UserReadUseCase {

    private final UserJpaRepository userRepository;

    @Override
    public User findById(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow();
    }
}
