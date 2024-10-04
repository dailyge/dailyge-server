package project.dailyge.app.core.anniversary.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;

public interface AnniversaryWriteService {
    Long save(DailygeUser dailygeUser, AnniversaryCreateCommand command);
}
