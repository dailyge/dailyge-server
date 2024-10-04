package project.dailyge.app.core.emoji.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.emoji.application.EmojiReadService;
import static project.dailyge.app.core.emoji.exception.EmojiCodeAndMessage.EMOJI_NOT_FOUND;
import project.dailyge.app.core.emoji.exception.EmojiTypeException;
import project.dailyge.entity.emoji.EmojiEntityReadRepository;

@RequiredArgsConstructor
@ApplicationLayer(value = "EmojiReadUseCase")
class EmojiReadUseCase implements EmojiReadService {

    private final EmojiEntityReadRepository emojiEntityReadRepository;

    @Override
    public void validateExists(final Long emojiId) {
        emojiEntityReadRepository.findById(emojiId)
            .orElseThrow(() -> EmojiTypeException.from(EMOJI_NOT_FOUND));
    }
}
