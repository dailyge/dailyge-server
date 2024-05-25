package project.dailyge.app.core.user.application.service;

import lombok.*;
import org.springframework.stereotype.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.domain.user.*;

@Service
@RequiredArgsConstructor
public class UserReadService implements UserReadUseCase {

    private final UserJpaRepository userRepository;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow();
    }
}
