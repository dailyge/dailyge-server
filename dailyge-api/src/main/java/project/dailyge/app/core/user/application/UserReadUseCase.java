package project.dailyge.app.core.user.application;

import project.dailyge.app.core.user.dto.response.UserInfoResponse;
import project.dailyge.domain.user.UserJpaEntity;

public interface UserReadUseCase {
    UserJpaEntity findById(Long userId);

    UserInfoResponse findUserInfoById(Long userId);
}
