package project.dailyge.app.test.weeklygoal.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalReadService;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.app.cursor.Cursor;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.app.cursor.Cursor.createCursor;

@DisplayName("[IntegrationTest] 주간 목표 조회 통합 테스트")
class WeeklyGoalReadIntegrationTest extends DatabaseTestBase {
    private static final String DEFAULT_TITLE = "주간 목표 조회 API 개발";
    private static final String DEFAULT_CONTENT = "주간 목표 조회 API 개발 내용";

    @Autowired
    private WeeklyGoalReadService weeklyGoalReadService;

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @Test
    @DisplayName("index가 존재하지 않으면, 가장 오래된 데이터를 반환한다.")
    void whenIndexNotExistsThenTheOldestDataShouldBeReturned() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        weeklyGoalWriteService.save(dailygeUser, createCommand);
        final Cursor cursor = createCursor(null, 10);
        final LocalDateTime weekStartDate = LocalDate.now().atTime(0, 0, 0, 0).with(DayOfWeek.MONDAY);
        final List<WeeklyGoalJpaEntity> findWeeklyGoals = weeklyGoalReadService.findPageByCursor(dailygeUser, cursor, weekStartDate);
        assertEquals(1, findWeeklyGoals.size());
    }

    @Test
    @DisplayName("index가 존재하면, 그 뒤의 데이터를 읽어온다.")
    void whenIndexExistsThenAfterIndexDataShouldBeReturned() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long firstWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        weeklyGoalWriteService.save(dailygeUser, createCommand);
        final LocalDateTime weekStartDate = LocalDate.now().atTime(0, 0, 0, 0).with(DayOfWeek.MONDAY);
        final Cursor cursor = createCursor(firstWeeklyGoalId, 10);
        final List<WeeklyGoalJpaEntity> findWeeklyGoals = weeklyGoalReadService.findPageByCursor(dailygeUser, cursor, weekStartDate);
        assertEquals(1, findWeeklyGoals.size());
    }
}
