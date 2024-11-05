package project.dailyge.entity.test.note

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import project.dailyge.entity.note.NoteJpaEntity
import java.time.LocalDateTime

@DisplayName("[UnitTest] Note 단위 테스트")
class NoteUnitTest : DescribeSpec({

    val fixedSentAt = LocalDateTime.of(2024, 11, 1, 10, 0)
    val fixedReadAt = LocalDateTime.of(2024, 11, 1, 12, 0)
    val fixedLastModifiedAt = LocalDateTime.of(2024, 11, 1, 15, 0)

    describe("쪽지 생성 단위 테스트") {
        val title = "프로젝트 회의 일정 공지"
        val content = "다음 주 화요일 오전 10시에 프로젝트 회의가 진행됩니다. 참석 부탁드립니다."
        context("객체를 생성할 때") {
            it("쪽지 객체가 정상적으로 생성된다") {
                val note = NoteJpaEntity(
                    title = title,
                    content = content,
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.title shouldBe title
                note.content shouldBe content
                note.isRead shouldBe false
                note.senderDeleted shouldBe false
                note.receiverDeleted shouldBe false
                note.sentAt shouldBe fixedSentAt
                note.readAt shouldBe null
            }

            it("주 생성자로 객체를 생성할 수 있다.") {
                val note = NoteJpaEntity(
                    id = 1L,
                    _title = title,
                    _content = content,
                    sentAt = LocalDateTime.now(),
                    senderId = 123L,
                    receiverId = 456L
                )
                note.id shouldNotBe null
                note shouldNotBe null

                val idNullNote = NoteJpaEntity(
                    _title = title,
                    _content = content,
                    sentAt = LocalDateTime.now(),
                    senderId = 123L,
                    receiverId = 456L
                )
                idNullNote shouldNotBe null
            }

            it("부 생성자로 객체를 생성할 수 있다.") {
                val newNoteV1 = NoteJpaEntity(
                    title = "공지사항 전달드립니다",
                    content = "2024년 11월 6일 부로 새로운 정책이 시행됩니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 300L
                )
                newNoteV1 shouldNotBe null

                val newNoteV2 = NoteJpaEntity(
                    title = "공지사항 전달드립니다",
                    content = "2024년 11월 6일 부로 새로운 정책이 시행됩니다.",
                    sentAt = fixedSentAt,
                    isRead = false,
                    senderId = 1L,
                    receiverId = 300L
                )
                newNoteV2 shouldNotBe null
            }
        }
    }

    describe("쪽지 권한 검사 단위 테스트") {
        val title = "프로젝트 회의 일정 공지"
        val content = "다음 주 화요일 오전 10시에 프로젝트 회의가 진행됩니다. 참석 부탁드립니다."
        val invalidUserId = 999999L
        val note = NoteJpaEntity(
            title = title,
            content = content,
            sentAt = fixedSentAt,
            senderId = 1L,
            receiverId = 2L
        )
        context("쪽지의 권한을 검사할 때") {
            it("발신자에 따라 권한을 판단할 수 있다.") {
                note.validateSender(1L) shouldBe true
                note.validateSender(invalidUserId) shouldBe false
            }

            it("수신자에 따라 권한을 판단할 수 있다.") {
                note.validateReceiver(2L) shouldBe true
                note.validateReceiver(invalidUserId) shouldBe false
            }

            it("발신자 또는 수신자에 따라 권한을 판단할 수 있다.") {
                note.validateAuth(1L) shouldBe true
                note.validateAuth(2L) shouldBe true
                note.validateAuth(invalidUserId) shouldBe false
            }
        }
    }

    describe("쪽지 읽음 상태 업데이트") {
        context("쪽지 상태를 변경할 때") {
            val sender = 1L
            val receiver = 2L
            it("쪽지를 읽지 않았고, 수신자가 쪽지를 확인했다면, true를 반환한다.") {
                val note = NoteJpaEntity(
                    title = "긴급 공지",
                    content = "오늘 오후 3시까지 기획안을 제출해 주시기 바랍니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.readByReceiver(receiver) shouldBe true
                note.readByReceiver(sender) shouldBe false
            }

            it("수신자가 쪽지를 이미 확인했다면 false를 반환한다.") {
                val note = NoteJpaEntity(
                    title = "긴급 공지",
                    content = "오늘 오후 3시까지 기획안을 제출해 주시기 바랍니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.updateReadStatus(true, fixedReadAt)

                note.readByReceiver(receiver) shouldBe false
                note.readByReceiver(sender) shouldBe false
            }

            it("수신자가 쪽지를 읽었을 때 읽음 상태가 true로, 읽은 시간 필드가 업데이트 된다.") {
                val note = NoteJpaEntity(
                    title = "긴급 공지",
                    content = "오늘 오후 3시까지 기획안을 제출해 주시기 바랍니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.updateReadStatus(
                    isRead = true,
                    readAt = fixedReadAt
                )

                note.isRead shouldBe true
                note.readAt shouldBe fixedReadAt
            }
        }
    }

    describe("쪽지 삭제 단위 테스트") {
        context("발신자가 쪽지를 삭제했을 때") {
            it("발신자 삭제 상태가 true로 변경되고, 수신자 삭제 상태는 변경되지 않는다.") {
                val note = NoteJpaEntity(
                    title = "회의 자료 준비",
                    content = "다음 주 월요일 회의 자료 준비 부탁드립니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.delete(
                    userId = 1L,
                    lastModifiedAt = fixedLastModifiedAt
                )

                note.senderDeleted shouldBe true
                note.receiverDeleted shouldBe false
                note.deleted shouldBe false
            }
        }

        context("수신자가 쪽지를 삭제했을 때") {
            it("수신자 삭제 상태가 true로 변경되고, 발신자 삭제 상태는 변경되지 않는다.") {
                val note = NoteJpaEntity(
                    title = "회의 자료 준비",
                    content = "다음 주 월요일 회의 자료 준비 부탁드립니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.delete(
                    userId = 2L,
                    lastModifiedAt = fixedLastModifiedAt
                )

                note.senderDeleted shouldBe false
                note.receiverDeleted shouldBe true
                note.deleted shouldBe false
            }
        }

        context("발신자와 수신자가 모두 삭제했을 때") {
            it("쪽지의 운영 칼럼이 삭제 상태가 된다.") {
                val note = NoteJpaEntity(
                    title = "회의 자료 준비",
                    content = "다음 주 월요일 회의 자료 준비 부탁드립니다.",
                    sentAt = fixedSentAt,
                    senderId = 1L,
                    receiverId = 2L
                )

                note.delete(
                    userId = 1L,
                    lastModifiedAt = fixedLastModifiedAt
                )
                note.delete(
                    userId = 2L,
                    lastModifiedAt = fixedLastModifiedAt
                )

                note.senderDeleted shouldBe true
                note.receiverDeleted shouldBe true
                note.deleted shouldBe true
            }
        }
    }
})
