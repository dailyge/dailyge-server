package project.dailyge.app.test.anniversary.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.app.core.emoji.exception.EmojiCodeAndMessage;
import project.dailyge.app.core.emoji.exception.EmojiTypeException;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@DisplayName("[IntegrationTest] 기념일 저장 통합 테스트")
class AnniversarySaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryFacade anniversaryFacade;

    @Autowired
    private AnniversaryWriteService anniversaryWriteService;

    @Autowired
    private AnniversaryReadService anniversaryReadService;

    @Test
    @DisplayName("기념일이 저장되면 ID가 null이 아니다.")
    void whenSaveAnniversaryThenIdShouldBeNotNull() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, command);

        assertNotNull(newAnniversaryId);

        final AnniversaryJpaEntity findAnniversary = anniversaryReadService.findById(newAnniversaryId);
        assertAll(
            () -> assertEquals(name, findAnniversary.getName()),
            () -> assertEquals(remind, findAnniversary.isRemind()),
            () -> assertEquals(now.toLocalDate(), findAnniversary.getDate().toLocalDate())
        );
    }

    @Test
    @DisplayName("이모티콘 ID가 존재할 때, 올바르지 않다면 EmojiTypeException이 발생한다.")
    void whenEmojiIdExistsButInvalidThenEmojiTypeExceptionShouldBeHappen() {
        final LocalDateTime now = LocalDateTime.now();
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final Long invalidEmojiId = Long.MAX_VALUE;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, invalidEmojiId);
        assertThatThrownBy(() -> anniversaryFacade.save(dailygeUser, command))
            .isInstanceOf(EmojiTypeException.class)
            .hasMessage(EmojiCodeAndMessage.EMOJI_NOT_FOUND.message());
    }

    @Test
    @DisplayName("중복된 기념일이 저장되면 AnniversaryTypeException이 발생한다.")
    void whenDuplicatedAnniversaryThenAnniversaryTypeExceptionShouldBeHappen() {
        final LocalDateTime now = LocalDateTime.now();
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        anniversaryWriteService.save(dailygeUser, command);

        assertThatThrownBy(() -> anniversaryWriteService.save(dailygeUser, command))
            .isInstanceOf(AnniversaryTypeException.class)
            .hasMessage(AnniversaryCodeAndMessage.DUPLICATED_ANNIVERSARY.message());
    }
}
