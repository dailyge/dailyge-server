package project.dailyge.entity.emoji;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

@Getter
@NoArgsConstructor
@Entity(name = "emojis")
public class EmojiJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emoji")
    private String emoji;

    public EmojiJpaEntity(final String emoji) {
        this.emoji = emoji;
    }

    public EmojiJpaEntity(
        final Long id,
        final String emoji
    ) {
        this.id = id;
        this.emoji = emoji;
    }
}
