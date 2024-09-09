package project.dailyge.app.notice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.notice.application.NoticeWriteUseCase;
import project.dailyge.app.notice.application.command.NoticeCreateCommand;
import project.dailyge.entity.notice.NoticeEntityWriteRepository;

@Service
@RequiredArgsConstructor
public class NoticeWriteService implements NoticeWriteUseCase {

    private final NoticeEntityWriteRepository noticeEntityWriteRepository;

    @Override
    @Transactional
    public Long save(final NoticeCreateCommand command) {
        return noticeEntityWriteRepository.save(command.toEntity());
    }
}
