package project.dailyge.app.notice.application;

import project.dailyge.app.notice.application.command.NoticeCreateCommand;

public interface NoticeWriteUseCase {
    Long save(NoticeCreateCommand command);
}
