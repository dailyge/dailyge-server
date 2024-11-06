package project.dailyge.app.test.note.integrationtest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import project.dailyge.common.ratelimiter.RateLimiterWriteService;
import project.dailyge.entity.note.NoteJpaEntity;
import project.dailyge.entity.user.Role;

import java.time.LocalDateTime;

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
    @DisplayName("받지 않은 쪽지를 조회하면 NoteTypeException이 발생한다.")
    void whenReadNotReceivedNoteThenNoteTypeExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        noteFacade.save(dailygeUser, command, 30);
        final Long invalidNoteId = Long.MAX_VALUE;

        assertThatThrownBy(() -> noteReadService.findReceivedNoteById(receivedDailygeUser, invalidNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 (받은) 쪽지를 조회하면 CommonException이 발생한다.")
    void whenUnAuthorizedUserReadReceivedNoteThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
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
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        final NoteJpaEntity findNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);

        assertNotNull(findNote);
    }

    @Test
    @DisplayName("수신자가 쪽지를 읽었다면, 읽은 상태/시간이 변경된다.")
    void whenReceiverReadNoteThenNoteStatusAndReadAtShouldBeChanged() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
        await().atMost(3, SECONDS)
            .until(
                () -> {
                    final NoteJpaEntity updatedNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
                    return updatedNote.isRead() && updatedNote.getReadAt() != null;
                }
            );

        final NoteJpaEntity findNote = noteReadService.findReceivedNoteById(receivedDailygeUser, newNoteId);
        assertAll(
            () -> assertTrue(findNote.isRead()),
            () -> assertNotNull(findNote.getReadAt())
        );
    }

    @Test
    @DisplayName("권한이 없는 사용자가 (보낸) 쪽지를 조회하면 CommonException이 발생한다.")
    void whenUnAuthorizedUserReadSentNoteThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final DailygeUser invalidDailygeUser = new DailygeUser(Long.MAX_VALUE, Role.NORMAL);

        assertThatThrownBy(() -> noteReadService.findSentNoteById(invalidDailygeUser, newNoteId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("보내지 않은 쪽지를 조회하면 NoteTypeException이 발생한다.")
    void whenReadNotSentNoteThenThenNoteTypeExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        noteFacade.save(dailygeUser, command, 30);
        final Long invalidNoteId = Long.MAX_VALUE;

        assertThatThrownBy(() -> noteReadService.findSentNoteById(dailygeUser, invalidNoteId))
            .isInstanceOf(NoteTypeException.class)
            .hasMessage(NOTE_NOT_FOUND.message());
    }

    @Test
    @DisplayName("쪽지를 전송했다면 (보낸 쪽지함에서) 쪽지를 조회할 수 있다.")
    void whenReadSentNoteThenResultShouldNotBeNull() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        final NoteJpaEntity findNote = noteReadService.findSentNoteById(dailygeUser, newNoteId);
        assertNotNull(findNote);
    }
}
