package project.dailyge.app.core.user.application;

import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

public interface UserReadUseCase {
    UserJpaEntity findById(Long userId);

    UserJpaEntity findActiveUserById(Long userId);

    UserJpaEntity findAuthorizedById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);

    boolean existsById(Long userId);
}
