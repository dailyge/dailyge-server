package project.dailyge.core.cache.user;

import java.util.Date;

public interface UserBlacklistReadRepository {

    Date getBlacklistById(Long userId);
}
