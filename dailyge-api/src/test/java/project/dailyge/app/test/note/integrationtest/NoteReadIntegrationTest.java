package project.dailyge.app.test.note.integrationtest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteReadService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_NOT_FOUND;
import project.dailyge.app.core.note.exception.NoteTypeException;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.paging.Cursor;
import static project.dailyge.app.paging.Cursor.createCursor;
import static project.dailyge.app.test.note.fixture.NoteCommandFixture.createNoteCommand;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;
import project.dailyge.entity.note.NoteJpaEntity;
import project.dailyge.entity.user.Role;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("[IntegrationTest] 쪽지 조회 통합 테스트")
class NoteReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private NoteReadService noteReadService;

    @Autowired
    private RateLimiterWriteService rateLimiterWriteService;

    @AfterEach
    void afterEachTest() {
        rateLimiterWriteService.deleteById(dailygeUser.getUserId());
    }

    @Test
    @DisplayName("쪽지를 ID로 조회할 때, 올바르지 않은 사용자라면 CommonException(UnAuthorizedException)이 발생한다.")
    void whenFindNoteByIdButWithInvalidIdThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final DailygeUser invalidDailygeUser = new DailygeUser(Long.MAX_VALUE, Role.NORMAL);

        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(invalidDailygeUser, newNoteId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("받지 않은 쪽지를 조회하면 NoteTypeException(NoteNotFoundException)이 발생한다.")
    void whenReadNotReceivedNoteThenNoteTypeExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 30);
        final Long invalidNoteId = Long.MAX_VALUE;

        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(receivedDailygeUser, invalidNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 (받은) 쪽지를 조회하면 CommonException(UnAuthorizedException)이 발생한다.")
    void whenUnAuthorizedUserReadReceivedNoteThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final DailygeUser invalidDailygeUser = new DailygeUser(Long.MAX_VALUE, Role.NORMAL);

        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(invalidDailygeUser, newNoteId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("쪽지를 받았다면 (받은 쪽지함에서) 쪽지를 조회할 수 있다.")
    void whenReadReceivedNoteThenResultShouldNotBeNull() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        final NoteJpaEntity findNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);

        assertNotNull(findNote);
    }

    @Test
    @DisplayName("수신자가 쪽지를 읽었다면, 읽은 상태/시간이 변경된다.")
    void whenReceiverReadNoteThenNoteStatusAndReadAtShouldBeChanged() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
        await().atMost(3, SECONDS)
            .until(() -> {
                final NoteJpaEntity updatedNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
                return updatedNote.isRead() && updatedNote.getReadAt() != null;
            });

        final NoteJpaEntity findNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
        assertAll(
            () -> assertTrue(findNote.isRead()),
            () -> assertNotNull(findNote.getReadAt())
        );
    }

    @Test
    @DisplayName("권한이 없는 사용자가 (보낸) 쪽지를 조회하면 CommonException(UnAuthorizedException)이 발생한다.")
    void whenUnAuthorizedUserReadSentNoteThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final DailygeUser invalidDailygeUser = new DailygeUser(Long.MAX_VALUE, Role.NORMAL);

        assertThatThrownBy(() -> noteReadService.findSentNotesById(invalidDailygeUser, newNoteId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("보내지 않은 쪽지를 조회하면 NoteTypeException(NoteNotFoundException)이 발생한다.")
    void whenReadNotSentNoteThenThenNoteTypeExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 30);
        final Long invalidNoteId = Long.MAX_VALUE;

        assertThatThrownBy(() -> noteReadService.findSentNotesById(dailygeUser, invalidNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("쪽지를 전송했다면 (보낸 쪽지함에서) 쪽지를 조회할 수 있다.")
    void whenReadSentNoteThenResultShouldNotBeNull() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        final NoteJpaEntity findNote = noteReadService.findSentNotesById(dailygeUser, newNoteId);
        assertNotNull(findNote);
    }

    @Test
    @DisplayName("Null Cursor로 보낸 쪽지를 조회할 때, 쪽지가 존재하면 size가 0보다 크다.")
    void whenSearchSavedSentNotesWithNullCursorThenResultSizeShouldBeOver0() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 30);
        final Cursor cursor = createCursor(null, 10);

        final List<NoteJpaEntity> findNote = noteReadService.findSentNotesById(dailygeUser, cursor);
        assertFalse(findNote.isEmpty());
    }

    @Test
    @DisplayName("Cursor로 보낸 쪽지를 조회할 때, 쪽지가 존재하지 않으면 size가 0이다.")
    void whenSearchSentNotesWithCursorThenResultSizeShouldBeOver0() {
        final Cursor cursor = createCursor(null, 10);
        final List<NoteJpaEntity> findNote = noteReadService.findSentNotesById(dailygeUser, cursor);
        assertEquals(0, findNote.size());
    }

    @Test
    @DisplayName("Cursor로 보낸 쪽지를 조회할 때, 쪽지가 존재하면 size가 0보다 크다.")
    void whenSearchSavedSentNotesWithCursorThenResultSizeShouldBeOver() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final Cursor cursor = createCursor(newNoteId + 1, 10);

        final List<NoteJpaEntity> findNote = noteReadService.findSentNotesById(dailygeUser, cursor);
        assertFalse(findNote.isEmpty());
    }

    @Test
    @DisplayName("Null Cursor로 받은 쪽지를 조회할 때, 쪽지가 존재하면 size가 0보다 크다.")
    void whenSearchSavedReceiverNotesWithNullCursorThenResultSizeShouldBeOver() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 30);
        final Cursor cursor = createCursor(null, 10);

        final List<NoteJpaEntity> findNote = noteReadService.findReceivedNotesById(receivedDailygeUser, cursor);
        assertFalse(findNote.isEmpty());
    }

    @Test
    @DisplayName("Cursor로 받은 쪽지를 조회할 때, 쪽지가 존재하면 size가 0보다 크다.")
    void whenSearchSavedReceiverNotesWithCursorThenResultSizeShouldBeOver() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final Cursor cursor = createCursor(newNoteId + 1, 10);

        final List<NoteJpaEntity> findNote = noteReadService.findReceivedNotesById(receivedDailygeUser, cursor);
        assertFalse(findNote.isEmpty());
    }

    @Test
    @DisplayName("Cursor로 받은 쪽지를 조회할 때, 쪽지가 존재하지 않으면 size가 0이다.")
    void whenSearchReceivedNotesWithCursorThenResultSizeShouldBeOver0() {
        final Cursor cursor = createCursor(null, 10);
        final List<NoteJpaEntity> findNote = noteReadService.findSentNotesById(dailygeUser, cursor);
        assertEquals(0, findNote.size());
    }
}
