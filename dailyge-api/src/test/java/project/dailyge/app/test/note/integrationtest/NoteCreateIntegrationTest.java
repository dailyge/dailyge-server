package project.dailyge.app.test.note.integrationtest;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.TOO_MANY_REQUEST;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.note.application.NoteReadService;
import project.dailyge.app.core.note.application.NoteWriteService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import project.dailyge.app.core.note.facade.NoteFacade;
import static project.dailyge.app.test.note.fixture.NoteCommandFixture.createNoteCommand;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;
import project.dailyge.entity.note.NoteJpaEntity;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@DisplayName("[IntegrationTest] 쪽지 생성 통합 테스트")
class NoteCreateIntegrationTest extends DatabaseTestBase {

    private static final Logger log = LoggerFactory.getLogger(NoteCreateIntegrationTest.class);

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private NoteReadService noteReadService;

    @Autowired
    private NoteWriteService noteWriteService;

    @Autowired
    private RateLimiterWriteService rateLimiterWriteService;

    @AfterEach
    void afterEachTest() {
        rateLimiterWriteService.deleteById(dailygeUser.getUserId());
    }

    @Test
    @DisplayName("일정 시간 동안 한 개의 쪽지만 보낼 수 있다.")
    void whenSendManyNotesThenOnlyOneCanBeSaved() throws InterruptedException {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        final int numberOfThreads = 10;
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);
        final ExecutorService executorService = newFixedThreadPool(numberOfThreads);

        for (int index = 0; index < numberOfThreads; index++) {
            executorService.submit(() -> {
                try {
                    noteFacade.save(dailygeUser, command, 30);
                } catch (Exception ex) {
                    log.error("Ex: {}", ex.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, SECONDS);
        executorService.shutdownNow();

        final List<NoteJpaEntity> notes = noteReadService.findAll();
        assertEquals(1, notes.size());
    }

    @Test
    @DisplayName("30초 이내에 여러 쪽지를 보내면 CommonException(TOO_MANY_REQUEST)이 발생한다.")
    void whenMultipleThreadsSaveNotesThenCommonExceptionShouldBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 30);
        assertThatThrownBy(() -> noteFacade.save(dailygeUser, command, 30))
            .isInstanceOf(CommonException.class)
            .hasMessage(TOO_MANY_REQUEST.message());
    }

    @Test
    @DisplayName("일정 시간 후, 쪽지를 전송하면 CommonException(TOO_MANY_REQUEST)이 발생하지 않는다.")
    void whenSendMessageAfterSpecificSecondsThenExceptionShouldNotBeHappen() {
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = createNoteCommand(dailygeUser, sentAt);
        noteFacade.save(dailygeUser, command, 1);
        Awaitility.await()
            .atLeast(3, SECONDS)
            .untilAsserted(() ->
                assertThatCode(() -> noteFacade.save(dailygeUser, command, 1))
                    .doesNotThrowAnyException()
            );
    }

    @Test
    @DisplayName("쪽지를 발송하면 쪽지가 저장된다.")
    void whenSendNoteThenIdShouldNotBeNull() {
        final UserJpaEntity receiver = new UserJpaEntity(3500L, "kmularise", "kmularise@gmail.com");
        final LocalDateTime sentAt = LocalDateTime.of(2024, 10, 11, 13, 0);

        final String title = "커피챗 신청합니다.";
        final String content = "2024년 10월 11일 13:00";
        final NoteJpaEntity newNote = new NoteJpaEntity(title, content, sentAt, dailygeUser.getId(), receiver.getId());
        final Long newNoteId = noteWriteService.save(dailygeUser, newNote);

        assertNotNull(newNoteId);
        assertAll(
            () -> assertEquals(title, newNote.getTitle()),
            () -> assertEquals(content, newNote.getContent()),
            () -> assertEquals(sentAt, newNote.getSentAt()),
            () -> assertEquals(dailygeUser.getId(), newNote.getSenderId()),
            () -> assertEquals(receiver.getId(), newNote.getReceiverId())
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
