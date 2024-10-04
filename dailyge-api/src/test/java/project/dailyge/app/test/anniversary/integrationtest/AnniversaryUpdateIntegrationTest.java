package project.dailyge.app.test.anniversary.integrationtest;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.application.command.AnniversaryUpdateCommand;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;
import static project.dailyge.entity.user.Role.ADMIN;
import static project.dailyge.entity.user.Role.NORMAL;

import java.time.LocalDateTime;

@DisplayName("[IntegrationTest] 기념일 삭제 통합 테스트")
class AnniversaryUpdateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryFacade anniversaryFacade;

    @Autowired
    private AnniversaryWriteService anniversaryWriteService;

    @Autowired
    private AnniversaryReadService anniversaryReadService;

    @Test
    @DisplayName("업데이트를 하면 내용이 반영된다.")
    void whenUpdateAnniversaryThenStatusShouldBeApplied() {
        final LocalDateTime now = now().truncatedTo(MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, createCommand);

        final LocalDateTime updatedDate = now().truncatedTo(MILLIS);
        final String updateName = "사진 찍기";
        final boolean updatedRemind = true;
        final AnniversaryUpdateCommand updateCommand = new AnniversaryUpdateCommand(updateName, updatedDate, updatedRemind, null);

        anniversaryFacade.update(dailygeUser, newAnniversaryId, updateCommand);
        final AnniversaryJpaEntity findAnniversary = anniversaryReadService.findById(newAnniversaryId);
        assertAll(
            () -> assertEquals(updateName, findAnniversary.getName()),
            () -> assertEquals(updatedDate.getYear(), findAnniversary.getDate().getYear()),
            () -> assertEquals(updatedDate.getMonth(), findAnniversary.getDate().getMonth()),
            () -> assertEquals(updatedDate.getDayOfMonth(), findAnniversary.getDate().getDayOfMonth()),
            () -> assertEquals(updatedRemind, findAnniversary.isRemind())
        );
    }

    @Test
    @DisplayName("권한이 존재하지 않으면 CommonException이 발생한다.")
    void whenUnAuthorizedThenCommonExceptionShouldBeHappen() {
        final LocalDateTime now = now().truncatedTo(MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, createCommand);

        final LocalDateTime updatedDate = now().truncatedTo(MILLIS);
        final String updateName = "사진 찍기";
        final boolean updatedRemind = true;
        final AnniversaryUpdateCommand updateCommand = new AnniversaryUpdateCommand(updateName, updatedDate, updatedRemind, null);

        final DailygeUser otherUser = new DailygeUser(Long.MAX_VALUE, NORMAL);
        assertThatThrownBy(() -> anniversaryFacade.update(otherUser, newAnniversaryId, updateCommand))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("관리자일 경우 CommonException이 발생하지 않는다.")
    void whenAdminExceptionShouldNotBeHappen() {
        final LocalDateTime now = now().truncatedTo(MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, createCommand);

        final LocalDateTime updatedDate = now().truncatedTo(MILLIS);
        final String updateName = "사진 찍기";
        final boolean updatedRemind = true;
        final AnniversaryUpdateCommand updateCommand = new AnniversaryUpdateCommand(updateName, updatedDate, updatedRemind, null);

        final DailygeUser otherUser = new DailygeUser(Long.MAX_VALUE, ADMIN);
        assertDoesNotThrow(() -> anniversaryFacade.update(otherUser, newAnniversaryId, updateCommand));
    }

    @Test
    @DisplayName("기념일이 존재하지 않을 경우 AnniversaryTypeException이 발생한다.")
    void whenAnniversaryNotExistsThenAnniversaryTypeExceptionShouldBeHappen() {
        final LocalDateTime updatedDate = now().truncatedTo(MILLIS);
        final String updateName = "사진 찍기";
        final boolean updatedRemind = true;
        final AnniversaryUpdateCommand updateCommand = new AnniversaryUpdateCommand(updateName, updatedDate, updatedRemind, null);

        final Long invalidId = Long.MAX_VALUE;
        assertThatThrownBy(() -> anniversaryFacade.update(dailygeUser, invalidId, updateCommand))
            .isInstanceOf(AnniversaryTypeException.class)
            .hasMessage(ANNIVERSARY_NOT_FOUND.message());
    }
}
