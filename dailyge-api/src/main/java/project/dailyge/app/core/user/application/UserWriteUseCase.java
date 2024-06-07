package project.dailyge.app.core.user.application;

import project.dailyge.domain.user.UserJpaEntity;

public interface UserWriteUseCase {
    UserJpaEntity save(UserJpaEntity user);

    void delete(Long user);
}
