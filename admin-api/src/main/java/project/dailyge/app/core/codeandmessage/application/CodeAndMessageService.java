package project.dailyge.app.core.codeandmessage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityReadRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteService;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import project.dailyge.entity.codeandmessage.CodeAndMessageReadService;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

import java.util.List;

@Service
@RequiredArgsConstructor
class CodeAndMessageService implements CodeAndMessageReadService, CodeAndMessageEntityWriteService {

    private final CodeAndMessageEntityReadRepository codeAndMessageReadRepository;
    private final CodeAndMessageEntityWriteRepository codeAndMessageWriteRepository;

    @Override
    public List<CodeAndMessageJpaEntity> findAll() {
        return codeAndMessageReadRepository.findAll();
    }

    @Override
    @Transactional
    public void saveAll(final CodeAndMessages codeAndMessages) {
        codeAndMessageWriteRepository.saveAll(codeAndMessages.getCodeAndMessages());
    }
}
