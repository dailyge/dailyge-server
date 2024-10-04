package project.dailyge.entity.anniversary;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AnniversaryEntityReadRepository {
    Optional<AnniversaryJpaEntity> findById(Long anniversaryId);

    boolean exists(String name, LocalDateTime date);
}
