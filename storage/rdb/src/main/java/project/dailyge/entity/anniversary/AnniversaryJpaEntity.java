package project.dailyge.entity.anniversary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "anniversaries")
public class AnniversaryJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "remind")
    private boolean remind;

    @Column(name = "emoji_id")
    private Long emojiId;

    @Column(name = "user_id")
    private Long userId;

    public AnniversaryJpaEntity(
        final String name,
        final LocalDateTime date,
        final boolean remind,
        final Long emojiId,
        final Long userId
    ) {
        this(null, name, date, remind, emojiId, userId);
    }

    public AnniversaryJpaEntity(
        final Long id,
        final String name,
        final LocalDateTime date,
        final boolean remind,
        final Long emojiId,
        final Long userId
    ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.remind = remind;
        this.emojiId = emojiId;
        this.userId = userId;
    }

    public void update(
        final String name,
        final LocalDateTime date,
        final boolean remind,
        final Long emojiId
    ) {
        this.name = name;
        this.date = date;
        this.remind = remind;
        this.emojiId = emojiId;
    }

    public void delete() {
        this.deleted = true;
    }
}
