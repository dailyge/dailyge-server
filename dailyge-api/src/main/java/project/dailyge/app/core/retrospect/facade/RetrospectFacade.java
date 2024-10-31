package project.dailyge.app.core.retrospect.facade;

import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;

@FacadeLayer(value = "RetrospectFacade")
public class RetrospectFacade {

    private final RetrospectWriteService retrospectWriteService;

    public RetrospectFacade(final RetrospectWriteService retrospectWriteService) {
        this.retrospectWriteService = retrospectWriteService;
    }

    public Long save(
        final DailygeUser dailygeUser,
        final RetrospectCreateCommand command
    ) {
        return retrospectWriteService.save(dailygeUser, command);
    }
}
