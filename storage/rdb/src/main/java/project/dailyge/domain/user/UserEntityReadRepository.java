package project.dailyge.domain.user;

import java.util.Optional;

public interface UserEntityReadRepository {
    Optional<UserJpaEntity> findById(Long userId);

    Optional<UserJpaEntity> findActiveById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);
}
