package project.dailyge.app.core.retrospect.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;

public interface RetrospectWriteService {
    Long save(DailygeUser dailygeUser, RetrospectCreateCommand command);
}
