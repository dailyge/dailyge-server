package project.dailyge.app.core.user.application.service;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.domain.user.*;

@Service
@RequiredArgsConstructor
public class UserWriteService implements UserWriteUseCase {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
