package project.dailyge.app.core.retrospect.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.core.retrospect.application.command.RetrospectUpdateCommand;

public interface RetrospectWriteService {
    Long save(DailygeUser dailygeUser, RetrospectCreateCommand command);

    void update(DailygeUser dailygeUser, RetrospectUpdateCommand command, Long retrospectId);

    void delete(DailygeUser dailygeUser, Long retrospectId);
}
