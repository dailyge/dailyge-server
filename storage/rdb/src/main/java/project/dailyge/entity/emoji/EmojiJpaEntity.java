package project.dailyge.entity.emoji;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "emojis")
public class EmojiJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emoji")
    private String emoji;

    @Enumerated(EnumType.STRING)
    private EmojiType emojiType;

    public EmojiJpaEntity(
        final Long id,
        final String emoji,
        final EmojiType emojiType
    ) {
        validate(emoji, emojiType);
        this.id = id;
        this.emoji = emoji;
        this.emojiType = emojiType;
    }

    private void validate(
        final String emoji,
        final EmojiType emojiType
    ) {
        if (emoji.isBlank()) {
            throw new IllegalArgumentException("올바른 이모티콘을 입력해주세요.");
        }
        if (emojiType == null) {
            throw new IllegalArgumentException("올바른 이모티콘 유형을 입력해주세요.");
        }
    }
}
