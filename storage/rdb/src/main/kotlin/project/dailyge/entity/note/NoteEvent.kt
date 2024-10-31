package project.dailyge.entity.note

import project.dailyge.entity.common.DomainEvent
import project.dailyge.entity.common.EventType
import java.time.LocalDateTime

class NoteEvent(
    publisher: Long,
    val noteId: Long,
    domain: String? = DOMAIN,
    eventId: String,
    eventType: EventType,
    publishCount: Int = 0,
    createdAt: LocalDateTime = LocalDateTime.now(),
) : DomainEvent(publisher, domain!!, eventId, eventType, publishCount, createdAt) {

    companion object {
        private const val DOMAIN = "notes"
    }

    constructor(
        publisher: Long,
        noteId: Long,
        eventId: String,
        eventType: EventType,
    ) : this(publisher, noteId, DOMAIN, eventId, eventType, 1)
}
