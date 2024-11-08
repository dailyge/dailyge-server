package project.dailyge.app.core.emoji.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.emoji.application.EmojiWriteService;
import project.dailyge.entity.emoji.EmojiEntityWriteRepository;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@ApplicationLayer(value = "EmojiWriteUseCase")
class EmojiWriteUseCase implements EmojiWriteService {

    private final EmojiEntityWriteRepository emojiEntityWriteRepository;

    public EmojiWriteUseCase(final EmojiEntityWriteRepository emojiEntityWriteRepository) {
        this.emojiEntityWriteRepository = emojiEntityWriteRepository;
    }

    @Override
    @Transactional
    public Long save(final EmojiJpaEntity emoji) {
        return emojiEntityWriteRepository.save(emoji);
    }
}
