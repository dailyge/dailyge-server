package project.dailyge.entity.note

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity
import java.time.LocalDateTime

@Entity(name = "notes")
class NoteJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title")
    private var _title: String,

    @Column(name = "content")
    private var _content: String,

    @Column(name = "is_read")
    private var _isRead: Boolean = false,

    @Column(name = "sent_at")
    val sentAt: LocalDateTime,

    @Column(name = "read_at")
    private var _readAt: LocalDateTime? = null,

    @Column(name = "sender_id")
    val senderId: Long,

    @Column(name = "receiver_id")
    val receiverId: Long,
) : BaseEntity() {

    @Column(name = "sender_deleted")
    private var _senderDeleted: Boolean = false

    @Column(name = "receiver_deleted")
    private var _receiverDeleted: Boolean = false

    constructor(
        title: String,
        content: String,
        sentAt: LocalDateTime,
        senderId: Long,
        receiverId: Long,
    ) : this(null, title, content, false, sentAt, null, senderId, receiverId) {
        updateCreatedBy(senderId)
    }

    val title: String
        get() = _title

    val content: String
        get() = _content

    val isRead: Boolean
        get() = _isRead

    val readAt: LocalDateTime?
        get() = _readAt

    val senderDeleted: Boolean
        get() = _senderDeleted

    val receiverDeleted: Boolean
        get() = _receiverDeleted

    fun validateAuth(userId: Long): Boolean {
        return senderId == userId
                || receiverId == userId
    }

    fun readByReceiver(receiverId: Long): Boolean {
        return !isRead
                && this.receiverId == receiverId
    }

    fun validateSender(userId: Long): Boolean {
        return senderId == userId
    }

    fun validateReceiver(userId: Long): Boolean {
        return receiverId == userId
    }

    fun updateReadStatus(
        isRead: Boolean,
        readAt: LocalDateTime,
    ) {
        this._isRead = isRead
        this._readAt = readAt
        updateLastModifiedInfo(receiverId, readAt)
    }

    fun delete(
        userId: Long,
        lastModifiedAt: LocalDateTime,
    ) {
        when (userId) {
            senderId -> _senderDeleted = true
            receiverId -> _receiverDeleted = true
            else -> return
        }
        _deleted = (_senderDeleted && _receiverDeleted)
        updateLastModifiedInfo(userId, lastModifiedAt)
    }
}
