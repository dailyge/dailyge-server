package project.dailyge.app.core.emoji.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.emoji.EmojiEntityWriteRepository;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@Repository
@RequiredArgsConstructor
class EmojiWriteDao implements EmojiEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final EmojiJpaEntity emoji) {
        entityManager.persist(emoji);
        return emoji.getId();
    }
}
