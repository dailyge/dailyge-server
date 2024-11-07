package project.dailyge.app.core.user.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;
import project.dailyge.entity.user.UserJpaEntity;

public interface UserWriteService {
    Long save(String email);

    UserJpaEntity save(UserJpaEntity user);

    void update(DailygeUser dailygeUser, UserUpdateCommand command);

    UserJpaEntity upsert(UserJpaEntity user);

    void delete(Long userId);
}
