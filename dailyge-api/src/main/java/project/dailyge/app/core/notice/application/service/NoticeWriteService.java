package project.dailyge.app.core.notice.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.notice.application.NoticeWriteUseCase;
import project.dailyge.app.core.notice.application.command.NoticeCreateCommand;
import project.dailyge.entity.notice.NoticeEntityWriteRepository;

@ApplicationLayer
@RequiredArgsConstructor
public class NoticeWriteService implements NoticeWriteUseCase {

    private final NoticeEntityWriteRepository noticeEntityWriteRepository;

    @Override
    public Long save(final NoticeCreateCommand command) {
        return noticeEntityWriteRepository.save(command.toEntity());
    }
}
