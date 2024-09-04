package project.dailyge.entity.codeandmessage;

import java.util.List;

public interface CodeAndMessageEntityWriteRepository {
    void saveAll(List<CodeAndMessageJpaEntity> codeAndMessages);
}
