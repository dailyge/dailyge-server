package project.dailyge.app.core.codeandmessage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityReadRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteService;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import project.dailyge.entity.codeandmessage.CodeAndMessageReadService;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

import java.util.List;

@ApplicationLayer
@RequiredArgsConstructor
class CodeAndMessageUseCase implements CodeAndMessageReadService, CodeAndMessageEntityWriteService {

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
