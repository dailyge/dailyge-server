package project.dailyge.app.core.notice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.notice.application.NoticeWriteService;
import project.dailyge.app.core.notice.application.command.NoticeCreateCommand;
import project.dailyge.entity.notice.NoticeEntityWriteRepository;

@Service
public class NoticeWriteUseCase implements NoticeWriteService {

    private final NoticeEntityWriteRepository noticeEntityWriteRepository;

    public NoticeWriteUseCase(final NoticeEntityWriteRepository noticeEntityWriteRepository) {
        this.noticeEntityWriteRepository = noticeEntityWriteRepository;
    }

    @Override
    @Transactional
    public Long save(final NoticeCreateCommand command) {
        return noticeEntityWriteRepository.save(command.toEntity());
    }
}
