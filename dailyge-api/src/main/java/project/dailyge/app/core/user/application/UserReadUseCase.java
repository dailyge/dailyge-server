package project.dailyge.app.core.user.application;

import project.dailyge.domain.user.User;

public interface UserReadUseCase {
    User findById(Long userId);
}
