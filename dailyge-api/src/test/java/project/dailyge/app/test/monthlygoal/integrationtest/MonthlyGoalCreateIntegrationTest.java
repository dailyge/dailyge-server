package project.dailyge.app.test.monthlygoal.integrationtest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;

import java.time.LocalDate;

@DisplayName("[IntegrationTest] 월간 목표 작성 통합 테스트")
class MonthlyGoalCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalWriteService monthlyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("월간 목표가 생성되면 Id가 Null이 아니다.")
    void whenCreateMonthlyGoalThenIdShouldBeNotNull() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        assertNotNull(monthlyGoalWriteService.save(dailygeUser, createCommand));
    }
}
