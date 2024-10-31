package project.dailyge.app.core.emoji.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.emoji.EmojiEntityWriteRepository;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@Repository
class EmojiWriteDao implements EmojiEntityWriteRepository {

    private final EntityManager entityManager;

    public EmojiWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final EmojiJpaEntity emoji) {
        entityManager.persist(emoji);
        return emoji.getId();
    }
}
