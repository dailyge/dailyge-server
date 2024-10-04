package project.dailyge.app.core.emoji.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.emoji.application.EmojiWriteService;
import project.dailyge.entity.emoji.EmojiEntityWriteRepository;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@RequiredArgsConstructor
@ApplicationLayer(value = "EmojiWriteUseCase")
class EmojiWriteUseCase implements EmojiWriteService {

    private final EmojiEntityWriteRepository emojiEntityWriteRepository;

    @Override
    @Transactional
    public Long save(final EmojiJpaEntity emoji) {
        return emojiEntityWriteRepository.save(emoji);
    }
}
