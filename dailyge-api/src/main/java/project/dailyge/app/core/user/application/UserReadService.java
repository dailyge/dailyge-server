package project.dailyge.app.core.user.application;

import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

public interface UserReadService {
    UserJpaEntity findById(Long userId);

    UserJpaEntity findActiveUserById(Long userId);

    UserJpaEntity findAuthorizedUserById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);

    UserJpaEntity findByNickname(String nickname);

    Long findUserIdByEmail(String email);

    String findEmailByUserId(Long userId);

    boolean existsById(Long userId);
}
