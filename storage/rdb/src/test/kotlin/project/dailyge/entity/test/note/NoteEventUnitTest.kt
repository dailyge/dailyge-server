package project.dailyge.entity.test.note

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import project.dailyge.entity.common.EventType
import project.dailyge.entity.note.NoteEvent
import java.time.LocalDateTime
import java.util.UUID.randomUUID

@DisplayName("[UnitTest] NoteEvent 단위 테스트")
class NoteEventUnitTest : DescribeSpec({

    val fixedCreatedAt = LocalDateTime.of(2024, 11, 1, 10, 0)
    val publisherId = 1L
    val noteId = 100L
    val eventId = randomUUID().toString()
    val eventType = EventType.CREATE

    describe("NoteEvent 생성 단위 테스트") {
        context("NoteEvent 객체를 생성할 때") {
            it("주 생성자로 객체가 정상적으로 생성된다") {
                val noteEvent = NoteEvent(
                    publisher = publisherId,
                    noteId = noteId,
                    eventId = eventId,
                    eventType = eventType,
                    createdAt = fixedCreatedAt
                )

                noteEvent.publisher shouldBe publisherId
                noteEvent.noteId shouldBe noteId
                noteEvent.eventId shouldBe eventId
                noteEvent.eventType shouldBe eventType
                noteEvent.domain shouldBe "notes"
                noteEvent.publishCount shouldBe 0
                noteEvent.createdAt shouldBe fixedCreatedAt
            }

            it("보조 생성자로 객체가 정상적으로 생성된다") {
                val noteEvent = NoteEvent(
                    publisher = publisherId,
                    noteId = noteId,
                    eventId = eventId,
                    eventType = eventType
                )

                noteEvent.publisher shouldBe publisherId
                noteEvent.noteId shouldBe noteId
                noteEvent.domain shouldBe "notes"
                noteEvent.eventId shouldBe eventId
                noteEvent.eventType shouldBe eventType
                noteEvent.domain shouldBe "notes"
                noteEvent.publishCount shouldBe 1
                noteEvent.createdAt shouldNotBe null
            }
        }
    }

    describe("NoteEvent 기본 값 검증") {
        context("기본 생성 값들을 확인할 때") {
            it("publishCount는 기본적으로 0으로 설정된다") {
                val noteEvent = NoteEvent(
                    publisher = publisherId,
                    noteId = noteId,
                    eventId = eventId,
                    eventType = eventType,
                    createdAt = fixedCreatedAt
                )
                noteEvent.publishCount shouldBe 0
            }

            it("createdAt은 기본적으로 현재 시간으로 설정된다") {
                val noteEvent = NoteEvent(
                    publisher = publisherId,
                    noteId = noteId,
                    eventId = eventId,
                    eventType = eventType
                )
                noteEvent.createdAt shouldNotBe null
            }
        }
    }
})
