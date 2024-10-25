package project.dailyge.app.core.retrospect.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;

@RequiredArgsConstructor
@FacadeLayer(value = "RetrospectFacade")
public class RetrospectFacade {

    private final RetrospectWriteService retrospectWriteService;

    public Long save(
        final DailygeUser dailygeUser,
        final RetrospectCreateCommand command
    ) {
        return retrospectWriteService.save(dailygeUser, command);
    }
}
