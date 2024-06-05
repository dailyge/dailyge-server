package project.dailyge.app.core.task.application.service;

import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.domain.task.TaskJpaEntity;

@Component
public class TaskValidator {

    public void validateAuth(
        final DailygeUser dailygeUser,
        final TaskJpaEntity findTask
    ) {
        if (dailygeUser.isAdmin()) {
            return;
        }
        if (!findTask.isOwner(dailygeUser.getUserId())) {
            throw new UnAuthorizedException();
        }
    }
}
