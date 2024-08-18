package project.dailyge.app.core.notice.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.notice.NoticeEntityWriteRepository;
import project.dailyge.entity.notice.NoticeJpaEntity;

@Repository
@RequiredArgsConstructor
public class NoticeWriteDao implements NoticeEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final NoticeJpaEntity notice) {
        entityManager.persist(notice);
        return notice.getId();
    }
}
