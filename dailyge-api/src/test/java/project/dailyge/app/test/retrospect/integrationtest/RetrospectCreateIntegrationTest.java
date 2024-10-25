package project.dailyge.app.test.retrospect.integrationtest;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 회고 저장 통합 테스트")
class RetrospectCreateIntegrationTest extends DatabaseTestBase {

    private static final String TITLE = "회고 제목";
    private static final String CONTENT = "회고 내용";

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @Autowired
    private RetrospectReadService retrospectReadService;

    @Test
    @DisplayName("회고를 저장하면 ID가 Null이 아니다.")
    void whenSaveRetrospectThenResultShouldBeNotNull() {
        final LocalDate now = LocalDate.now();
        final RetrospectCreateCommand command = new RetrospectCreateCommand(TITLE, CONTENT, now.atTime(0, 0, 0, 0), true);

        final Long retrospectId = retrospectWriteService.save(dailygeUser, command);
        final RetrospectJpaEntity findRetrospect = retrospectReadService.findById(retrospectId);

        assertAll(
            () -> assertEquals(retrospectId, findRetrospect.getId()),
            () -> assertEquals(TITLE, findRetrospect.getTitle()),
            () -> assertEquals(CONTENT, findRetrospect.getContent()),
            () -> assertEquals(now.atTime(0, 0, 0, 0), findRetrospect.getDate()),
            () -> assertTrue(findRetrospect.isPublic())
        );
    }
}
