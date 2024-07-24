package project.dailyge.entity.codeandmessage;

import java.util.List;

public interface CodeAndMessageEntityWriteRepository {
    List<CodeAndMessageJpaEntity> saveAll(List<CodeAndMessageJpaEntity> codeAndMessages);
}
