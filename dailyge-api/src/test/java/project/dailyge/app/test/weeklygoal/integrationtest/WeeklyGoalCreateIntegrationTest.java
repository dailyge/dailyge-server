package project.dailyge.app.test.weeklygoal.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;

import java.time.LocalDate;

import static org.bson.assertions.Assertions.assertNotNull;

@DisplayName("[IntegrationTest] 주간 목표 작성 통합 테스트")
class WeeklyGoalCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("주간 목표가 생성되면 Id가 null이 아니다.")
    void whenCreateWeeklyGoalThenIdShouldBeNotNull() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand("주간 목표 제목", "주간 목표 내용", now);
        assertNotNull(weeklyGoalWriteService.save(dailygeUser, createCommand));
    }
}
