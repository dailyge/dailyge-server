package project.dailyge.core.cache.user;

import java.util.Date;

public interface UserBlacklistReadUseCase {

    Date getBlacklistById(Long userId);
}
