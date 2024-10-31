package project.dailyge.app.test.note.integrationtest;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.note.application.NoteWriteService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.entity.note.NoteJpaEntity;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@DisplayName("[IntegrationTest] 쪽지 생성 통합 테스트")
class NoteCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private NoteWriteService noteWriteService;

    @Autowired
    private UserWriteService userWriteService;

    @Test
    @DisplayName("멀티 스레드 환경에서 쪽지를 발송해도 에러가 발생하지 않는다.")
    void whenMultipleThreadsSaveNotesThenHandleCorrectly() throws InterruptedException {
        final UserJpaEntity receiver = new UserJpaEntity(300L, "Receiver", "dailyge-receiver@gmail.com");
        userWriteService.save(receiver);
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "Receiver", dailygeUser.getId()
        );
        final int numberOfThreads = 10;
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);
        final ExecutorService executorService = newFixedThreadPool(numberOfThreads);

        for (int index = 0; index < numberOfThreads; index++) {
            executorService.submit(() -> {
                try {
                    assertDoesNotThrow(() -> noteFacade.save(dailygeUser, command));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }

    @Test
    @DisplayName("쪽지를 발송하면 쪽지가 저장된다.")
    void whenSendNoteThenIdShouldNotBeNull() {
        final UserJpaEntity receiver = new UserJpaEntity(300L, "Receiver", "dailyge-receiver@gmail.com");
        final UserJpaEntity newReceiver = userWriteService.save(receiver);
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);

        final String title = "커피챗 신청합니다.";
        final String content = "2024년 10월 11일 13:00";
        final NoteJpaEntity newNote = new NoteJpaEntity(title, content, sentAt, dailygeUser.getId(), newReceiver.getId());
        final Long newNoteId = noteWriteService.save(dailygeUser, newNote);

        assertNotNull(newNoteId);
        assertAll(
            () -> assertEquals(title, newNote.getTitle()),
            () -> assertEquals(content, newNote.getContent()),
            () -> assertEquals(sentAt, newNote.getSentAt()),
            () -> assertEquals(dailygeUser.getId(), newNote.getSenderId()),
            () -> assertEquals(newReceiver.getId(), newNote.getReceiverId())
        );
    }

    @Test
    @DisplayName("발신자와 수신자가 같다면 IllegalArgumentException이 발생한다.")
    void whenSenderEqualsReceiverThenIllegalArgumentExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);

        final String title = "커피챗 신청합니다.";
        final String content = "2024년 10월 11일 13:00";
        final NoteJpaEntity newNote = new NoteJpaEntity(title, content, sentAt, dailygeUser.getId(), dailygeUser.getId());
        assertThrows(IllegalArgumentException.class,
            () -> noteWriteService.save(dailygeUser, newNote)
        );
    }
}
