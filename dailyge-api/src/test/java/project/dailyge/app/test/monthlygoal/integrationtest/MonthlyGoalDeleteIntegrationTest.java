package project.dailyge.app.test.monthlygoal.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadUseCase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;

@DisplayName("[IntegrationTest] 월간 목표 삭제 통합 테스트")
class MonthlyGoalDeleteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalReadUseCase monthlyGoalReadUseCase;

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @Test
    @DisplayName("삭제된 월간 목표를 조회하면 MonthlyGoalTypeException이 발생한다.")
    void whenSearchDeletedMonthlyGoalThenNotFoundExceptionShouldBeHappen() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.");
        final Long newMonthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        monthlyGoalWriteUseCase.delete(dailygeUser, newMonthlyGoalId);

        assertThatThrownBy(() -> monthlyGoalReadUseCase.findById(newMonthlyGoalId))
            .isInstanceOf(MonthlyGoalTypeException.class)
            .hasMessage(MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND.getMessage());
    }
}
