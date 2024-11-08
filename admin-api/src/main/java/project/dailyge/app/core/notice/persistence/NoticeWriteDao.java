package project.dailyge.app.core.notice.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.notice.NoticeEntityWriteRepository;
import project.dailyge.entity.notice.NoticeJpaEntity;

@Repository
public class NoticeWriteDao implements NoticeEntityWriteRepository {

    private final EntityManager entityManager;

    public NoticeWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Long save(final NoticeJpaEntity notice) {
        entityManager.persist(notice);
        return notice.getId();
    }
}
