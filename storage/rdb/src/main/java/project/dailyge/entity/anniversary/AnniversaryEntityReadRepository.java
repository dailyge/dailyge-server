package project.dailyge.entity.anniversary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnniversaryEntityReadRepository {
    Optional<AnniversaryJpaEntity> findById(Long anniversaryId);

    boolean exists(String name, LocalDateTime date);

    List<AnniversaryJpaEntity> findByDates(Long userId, LocalDate startDate, LocalDate endDate);
}
