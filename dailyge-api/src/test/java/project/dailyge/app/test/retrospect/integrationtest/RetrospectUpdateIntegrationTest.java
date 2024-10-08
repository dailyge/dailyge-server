package project.dailyge.app.test.retrospect.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.core.retrospect.application.command.RetrospectUpdateCommand;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static project.dailyge.entity.user.Role.ADMIN;
import static project.dailyge.entity.user.Role.NORMAL;

import java.time.LocalDate;

@DisplayName("[IntegrationTest] 회고 수정 통합 테스트")
class RetrospectUpdateIntegrationTest extends DatabaseTestBase {

    private static final String UPDATE_TITLE = "회고 제목 수정";
    private static final String UPDATE_CONTENT = "회고 내용 수정";
    private RetrospectCreateCommand createCommand;
    private RetrospectUpdateCommand updateCommand;
    private LocalDate now;

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @Autowired
    private RetrospectReadService retrospectReadService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(0, 0, 0, 0), false);
        updateCommand = new RetrospectUpdateCommand(UPDATE_TITLE, UPDATE_CONTENT, now.atTime(0, 0, 0, 0), true);
    }

    @Test
    @DisplayName("회고를 수정하면 정상적으로 수정된다.")
    void whenUpdateRetrospectThenResultShouldBeUpdate() {
        final LocalDate now = LocalDate.now();
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        retrospectWriteService.update(dailygeUser, updateCommand, retrospectId);
        final RetrospectJpaEntity findRetrospect = retrospectReadService.findById(retrospectId);

        assertAll(
            () -> assertEquals(retrospectId, findRetrospect.getId()),
            () -> assertEquals(UPDATE_TITLE, findRetrospect.getTitle()),
            () -> assertEquals(UPDATE_CONTENT, findRetrospect.getContent()),
            () -> assertEquals(now.atTime(0, 0, 0, 0), findRetrospect.getDate()),
            () -> assertTrue(findRetrospect.isPublic())
        );
    }

    @Test
    @DisplayName("권한이 존재하지 않으면, CommonException이 발생한다.")
    void whenUnAuthorizedThenCommonExceptionShouldBeHappen() {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        final DailygeUser otherUser = new DailygeUser(Long.MAX_VALUE, NORMAL);

        assertThatThrownBy(() -> retrospectWriteService.update(otherUser, updateCommand, retrospectId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("관리자가 사용자의 회고를 수정하면, CommonException이 발생하지 않는다.")
    void whenUpdateOtherUserRetrospectByAdminThenExceptionShouldNotBeHappen() {
        final Long retrospectId = retrospectWriteService.save(dailygeUser, createCommand);

        final DailygeUser adminUser = new DailygeUser(Long.MAX_VALUE, ADMIN);

        assertDoesNotThrow(() -> retrospectWriteService.update(adminUser, updateCommand, retrospectId));
    }
}
