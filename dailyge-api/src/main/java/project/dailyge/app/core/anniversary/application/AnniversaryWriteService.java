package project.dailyge.app.core.anniversary.application;

import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.application.command.AnniversaryUpdateCommand;
import project.dailyge.app.core.common.auth.DailygeUser;

public interface AnniversaryWriteService {
    Long save(DailygeUser dailygeUser, AnniversaryCreateCommand command);

    void update(DailygeUser dailygeUser, Long anniversaryId, AnniversaryUpdateCommand command);

    void delete(DailygeUser dailygeUser, Long anniversaryId);
}
