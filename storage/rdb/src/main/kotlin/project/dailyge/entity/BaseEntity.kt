package project.dailyge.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at")
    protected var _createdAt: LocalDateTime? = null

    @Column(name = "created_by")
    protected var _createdBy: Long? = null

    @LastModifiedDate
    @Column(name = "last_modified_at")
    protected var _lastModifiedAt: LocalDateTime? = null

    @Column(name = "last_modified_by")
    protected var _lastModifiedBy: Long? = null

    @Column(name = "deleted")
    protected var _deleted: Boolean = false

    val createdAt: LocalDateTime?
        get() = _createdAt

    val createdAtAsString: String?
        get() = createdAt?.toString()

    val createdBy: Long?
        get() = _createdBy

    val lastModifiedAt: LocalDateTime?
        get() = _lastModifiedAt

    val lastModifiedAtAsString: String?
        get() = lastModifiedAt?.toString()

    val lastModifiedBy: Long?
        get() = _lastModifiedBy

    val deleted: Boolean
        get() = _deleted

    fun init(
        createdAt: LocalDateTime,
        createdBy: Long,
        lastModifiedAt: LocalDateTime? = null,
        lastModifiedBy: Long? = null,
        deleted: Boolean,
    ) {
        this._createdAt = createdAt
        this._createdBy = createdBy
        this._lastModifiedAt = lastModifiedAt
        this._lastModifiedBy = lastModifiedBy
        this._deleted = deleted
    }

    final fun updateCreatedBy(createdBy: Long) {
        this._createdBy = createdBy
    }

    fun updateLastModifiedInfo(
        lastModifiedBy: Long,
        lastModifiedAt: LocalDateTime,
    ) {
        this._lastModifiedBy = lastModifiedBy
        this._lastModifiedAt = lastModifiedAt
    }

    final fun updateDeletedStatus() {
        this._deleted = true
    }
}
