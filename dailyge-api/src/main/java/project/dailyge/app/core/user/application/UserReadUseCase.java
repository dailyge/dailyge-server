package project.dailyge.app.core.user.application;

import project.dailyge.domain.user.*;

public interface UserReadUseCase {
    User findById(Long userId);
}
