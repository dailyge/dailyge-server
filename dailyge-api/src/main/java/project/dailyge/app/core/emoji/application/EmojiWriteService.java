package project.dailyge.app.core.emoji.application;

import project.dailyge.entity.emoji.EmojiJpaEntity;

public interface EmojiWriteService {
    Long save(EmojiJpaEntity emoji);
}
