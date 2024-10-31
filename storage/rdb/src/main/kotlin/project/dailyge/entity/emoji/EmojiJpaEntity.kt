package project.dailyge.entity.emoji

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity

@Entity(name = "emojis")
class EmojiJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "emoji")
    val emoji: String,

    @Enumerated(EnumType.STRING)
    val emojiType: EmojiType,
) : BaseEntity() {

    init {
        validate(emoji, emojiType)
    }

    private fun validate(
        emoji: String,
        emojiType: EmojiType,
    ) {
        require(emoji.isNotBlank()) { "올바른 이모티콘을 입력해주세요." }
    }
}
