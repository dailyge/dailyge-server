package project.dailyge.app.test.note.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteReadService;
import project.dailyge.app.core.note.application.NoteWriteService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_NOT_FOUND;
import project.dailyge.app.core.note.exception.NoteTypeException;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDateTime;

@DisplayName("[IntegrationTest] 쪽지 삭제 통합 테스트")
class NoteDeleteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private NoteReadService noteReadService;

    @Autowired
    private NoteWriteService noteWriteService;

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private UserWriteService userWriteService;

    private DailygeUser sentDailygeUser;            // 발신자(beatmejy)
    private DailygeUser receivedDailygeUser;        // 수신자(kmularise)

    @BeforeEach
    void setUp() {
        sentDailygeUser = new DailygeUser(dailygeUser.getUserId(), dailygeUser.getRole());

        final UserJpaEntity receiver = new UserJpaEntity(300L, "kmularise", "kmularise@gmail.com");
        userWriteService.save(receiver);
        receivedDailygeUser = new DailygeUser(receiver.getId(), receiver.getRole());
    }

    @Test
    @DisplayName("수신자가 삭제한 받은 쪽지를 조회하면, NoteTypeException이 발생한다.")
    void whenReceiverDeleteNoteAndAfterFindThenNoteTypeExceptionShouldBeHappen() {
        // given: 발신자가 쪽지 전송
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", sentDailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(sentDailygeUser, command, 30);

        // when: 수신자가 받은 쪽지 삭제
        noteWriteService.deleteReceivedNoteById(receivedDailygeUser, newNoteId);

        // then: 수신자가 삭제된 쪽지 조회
        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(receivedDailygeUser.getUserId(), newNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("수신자가 받은 쪽지를 삭제했더라도, 발신자는 보낸 쪽지를 조회할 수 있다.")
    void whenReceiverDeleteNoteThenSenderCanFindNote() {
        // given: 발신자가 쪽지 전송
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", sentDailygeUser.getId()
        );

        // when: 수신자가 받은 쪽지 삭제
        final Long newNoteId = noteFacade.save(sentDailygeUser, command, 30);

        noteWriteService.deleteReceivedNoteById(receivedDailygeUser, newNoteId);

        // then: 발신자가 쪽지 조회
        assertDoesNotThrow(() ->
            noteReadService.findSentNoteById(sentDailygeUser.getUserId(), newNoteId)
        );
    }

    @Test
    @DisplayName("발신자가 삭제한 보낸 쪽지를 조회하면, NoteTypeException이 발생한다.")
    void whenSenderDeleteNoteAndAfterFindThenNoteTypeExceptionShouldBeHappen() {
        // given: 발신자가 쪽지 전송
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", sentDailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(sentDailygeUser, command, 30);

        // when: 발신자가 보낸 쪽지 삭제
        noteWriteService.deleteSentNoteById(sentDailygeUser, newNoteId);

        // then: 발신자가 삭제된 쪽지 조회
        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(sentDailygeUser.getUserId(), newNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("발신자가 보낸 쪽지를 삭제했더라도, 수신자는 받은 쪽지를 조회할 수 있다.")
    void whenSenderDeleteNoteThenReceiverCanFindNote() {
        // given: 발신자가 쪽지 전송
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", sentDailygeUser.getId()
        );

        // when: 발신자가 보낸 쪽지 삭제
        final Long newNoteId = noteFacade.save(sentDailygeUser, command, 30);

        noteWriteService.deleteSentNoteById(sentDailygeUser, newNoteId);

        // then: 수신자가 받은 쪽지 조회
        assertDoesNotThrow(() ->
            noteReadService.findReceivedNoteById(receivedDailygeUser.getUserId(), newNoteId)
        );
    }
}
