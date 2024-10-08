package project.dailyge.app.test.retrospect.integrationtest;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.core.retrospect.exception.RetrospectTypeException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_NOT_FOUND;
import static project.dailyge.entity.user.Role.ADMIN;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[IntegrationTest] 회고 삭제 통합 테스트")
class RetrospectDeleteIntegrationTest extends DatabaseTestBase {

    private RetrospectCreateCommand createCommand;

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @Autowired
    private RetrospectReadService retrospectReadService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(0, 0, 0, 0), false);
    }

    @Test
    @DisplayName("삭제된 회고를 조회하면, RetrospectTypeException이 발생한다.")
    void whenDeleteRetrospectThenResultShouldBeUpdate() {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        retrospectWriteService.delete(dailygeUser, retrospectId);

        assertThatThrownBy(() -> retrospectReadService.findById(retrospectId))
            .isInstanceOf(RetrospectTypeException.class)
            .hasMessage(RETROSPECT_NOT_FOUND.message());

    }

    @Test
    @DisplayName("권한이 존재하지 않으면, CommonException이 발생한다.")
    void whenUnAuthorizedThenCommonExceptionShouldBeHappen() {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        final DailygeUser otherUser = new DailygeUser(Long.MAX_VALUE, NORMAL);

        assertThatThrownBy(() -> retrospectWriteService.delete(otherUser, retrospectId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("관리자가 사용자의 회고를 삭제하면, CommonException이 발생하지 않는다.")
    void whenUpdateOtherUserRetrospectByAdminThenExceptionShouldNotBeHappen() {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        final DailygeUser adminUser = new DailygeUser(Long.MAX_VALUE, ADMIN);

        assertDoesNotThrow(() -> retrospectWriteService.delete(adminUser, retrospectId));
    }
}
