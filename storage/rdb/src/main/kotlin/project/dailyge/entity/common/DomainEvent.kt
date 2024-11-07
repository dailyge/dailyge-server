package project.dailyge.entity.common

import java.time.LocalDateTime

abstract class DomainEvent(
    val publisher: Long,
    val domain: String,
    val eventId: String,
    val eventType: EventType,
    val publishCount: Int = 0,
    val createdAt: LocalDateTime,
) {
    fun isType(eventType: EventType): Boolean {
        return this.eventType == eventType
    }

    companion object {
        const val INVALID_PUBLISHER_ID_ERROR_MESSAGE = "올바른 Publisher Id를 입력해주세요."
        const val INVALID_EVENT_ID_ERROR_MESSAGE = "올바른 Event Id를 입력해주세요."
        const val INVALID_EVENT_TYPE_ERROR_MESSAGE = "올바른 Event Type을 입력해주세요."
    }
}

