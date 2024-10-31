package project.dailyge.entity.user;

import java.util.Optional;

public interface UserEntityReadRepository {
    Optional<UserJpaEntity> findById(Long userId);

    Optional<UserJpaEntity> findActiveUserById(Long userId);

    Optional<UserJpaEntity> findActiveUserByEmail(String email);

    Optional<UserJpaEntity> findByNickname(String nickname);

    Long findUserIdByEmail(String email);

    String findEmailById(Long userId);
}
