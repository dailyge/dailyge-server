package project.dailyge.entity.codeandmessage;

import java.util.List;

public interface CodeAndMessageEntityReadRepository {
    List<CodeAndMessageJpaEntity> findAll();
}
