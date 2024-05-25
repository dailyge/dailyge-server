package project.dailyge.app.core.user.application;

import project.dailyge.domain.user.*;

public interface UserWriteUseCase {
    User save(User user);
}
