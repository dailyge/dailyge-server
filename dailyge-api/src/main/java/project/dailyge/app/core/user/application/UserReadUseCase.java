package project.dailyge.app.core.user.application;

import project.dailyge.app.core.user.dto.response.UserInfoResponse;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Optional;

public interface UserReadUseCase {
    UserJpaEntity findById(Long userId);

    UserJpaEntity findAuthorizedById(Long userId);

    UserInfoResponse findUserInfoById(Long userId);

    Optional<UserJpaEntity> findByEmail(String email);
}
