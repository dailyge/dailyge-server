package project.dailyge.entity.anniversary

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity
import java.time.LocalDateTime

@Entity(name = "anniversaries")
class AnniversaryJpaEntity(
    @Column(name = "name")
    private var _name: String,

    @Column(name = "date")
    private var _date: LocalDateTime,

    @Column(name = "remind")
    private var _remind: Boolean? = false,

    @Column(name = "emoji_id")
    private var _emojiId: Long? = null,

    @Column(name = "user_id")
    val userId: Long,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val name: String
        get() = _name

    val date: LocalDateTime
        get() = _date

    val dateAsString: String
        get() = _date.toLocalDate().toString()

    val remind: Boolean
        get() = _remind ?: false

    val emojiId: Long?
        get() = _emojiId

    fun update(
        name: String,
        date: LocalDateTime,
        remind: Boolean,
        emojiId: Long? = null,
    ) {
        this._name = name
        this._date = date
        this._remind = remind
        this._emojiId = emojiId
    }

    fun delete() {
        updateDeletedStatus()
    }
}
