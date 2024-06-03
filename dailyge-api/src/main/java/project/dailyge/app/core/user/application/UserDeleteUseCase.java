package project.dailyge.app.core.user.application;

import project.dailyge.domain.user.UserJpaEntity;

public interface UserDeleteUseCase {

    void delete(UserJpaEntity user);
}
