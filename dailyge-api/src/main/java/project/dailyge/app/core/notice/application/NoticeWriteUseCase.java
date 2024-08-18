package project.dailyge.app.core.notice.application;

import project.dailyge.app.core.notice.application.command.NoticeCreateCommand;

public interface NoticeWriteUseCase {
    Long save(NoticeCreateCommand command);
}
