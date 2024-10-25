package project.dailyge.entity.emoji;

import java.util.Optional;

public interface EmojiEntityReadRepository {
    Optional<EmojiJpaEntity> findById(Long emojiId);
}
