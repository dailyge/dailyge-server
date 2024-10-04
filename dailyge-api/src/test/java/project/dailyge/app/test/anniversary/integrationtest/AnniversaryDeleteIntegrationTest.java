package project.dailyge.app.test.anniversary.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.entity.user.Role;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@DisplayName("[IntegrationTest] 기념일 삭제 통합 테스트")
class AnniversaryDeleteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryWriteService anniversaryWriteService;

    @Autowired
    private AnniversaryReadService anniversaryReadService;

    @Test
    @DisplayName("삭제된 기념일을 조회하면 AnniversaryTypeException이 발생한다.")
    void whenDeletedAnniversaryThenAnniversaryTypeExceptionShouldBeHappen() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, command);

        anniversaryWriteService.delete(dailygeUser, newAnniversaryId);

        assertThatThrownBy(() -> anniversaryReadService.findById(newAnniversaryId))
            .isInstanceOf(AnniversaryTypeException.class)
            .hasMessage(AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND.message());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 기념일을 삭제하면 CommonException이 발생한다.")
    void whenUnAuthorizedThenCommonExceptionShouldBeHappen() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, command);

        final DailygeUser otherUser = new DailygeUser(Long.MAX_VALUE, Role.NORMAL);

        assertThatThrownBy(() -> anniversaryWriteService.delete(otherUser, newAnniversaryId))
            .isInstanceOf(CommonException.class)
            .hasMessage(CommonCodeAndMessage.UN_AUTHORIZED.message());
    }
}
