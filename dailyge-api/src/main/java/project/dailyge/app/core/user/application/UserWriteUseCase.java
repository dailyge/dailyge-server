package project.dailyge.app.core.user.application;

import project.dailyge.entity.user.UserJpaEntity;

public interface UserWriteUseCase {
    Long save(String email);

    UserJpaEntity save(UserJpaEntity user);

    UserJpaEntity upsert(UserJpaEntity user);

    void delete(Long userId);
}
