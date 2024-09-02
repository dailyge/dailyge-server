package project.dailyge.app.test.monthlygoal.integrationtest;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadUseCase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.cursor.Cursor;
import static project.dailyge.app.cursor.Cursor.createCursor;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

import java.time.LocalDate;
import java.util.List;

@DisplayName("[IntegrationTest] 월간 목표 조회 통합 테스트")
class MonthlyGoalReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalReadUseCase monthlyGoalReadUseCase;

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("index가 존재하지 않으면, 가장 오래된 데이터를 반환한다.")
    void whenIndexNotExistsThenTheOldestDataShouldBeReturned() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        final Cursor cursor = createCursor(null, 10);
        final int year = now.getYear();
        final int month = now.getMonthValue();

        final List<MonthlyGoalJpaEntity> findMonthlyGoals = monthlyGoalReadUseCase.findMonthlyGoalsByCursor(dailygeUser, cursor, year, month);
        assertEquals(1, findMonthlyGoals.size());
    }

    @Test
    @DisplayName("index가 존재하면, 그 뒤의 데이터를 읽어온다.")
    void whenIndexExistsThenAfterIndexDataShouldBeReturned() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        final Long firstMonthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        final Cursor cursor = createCursor(firstMonthlyGoalId, 10);
        final int year = now.getYear();
        final int month = now.getMonthValue();

        final List<MonthlyGoalJpaEntity> findMonthlyGoals = monthlyGoalReadUseCase.findMonthlyGoalsByCursor(dailygeUser, cursor, year, month);
        assertEquals(1, findMonthlyGoals.size());
    }
}
