package project.dailyge.entity.user;

import java.util.Optional;

public interface UserEntityReadRepository {
    Optional<UserJpaEntity> findById(Long userId);

    Optional<UserJpaEntity> findActiveUserById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);

    Long findUserIdByEmail(String email);

    String findEmailById(Long userId);
}
