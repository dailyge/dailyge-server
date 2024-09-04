package project.dailyge.app.core.user.application;

import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

public interface UserReadUseCase {
    UserJpaEntity findById(Long userId);

    UserJpaEntity findActiveUserById(Long userId);

    UserJpaEntity findAuthorizedUserById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);

    Long findUserIdByEmail(String email);

    String findEmailByUserId(Long userId);

    boolean existsById(Long userId);
}
