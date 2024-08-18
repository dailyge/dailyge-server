package project.dailyge.app.test.monthlygoal.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalReadUseCase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalStatusUpdateCommand;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalUpdateCommand;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalCodeAndMessage;
import project.dailyge.app.core.monthlygoal.exception.MonthlyGoalTypeException;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

@DisplayName("[IntegrationTest] 월간 목표 삭제 통합 테스트")
class MonthlyGoalUpdateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalReadUseCase monthlyGoalReadUseCase;

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @Test
    @DisplayName("MonthlyGoal 상태를 업데이트하면, 상태가 변경된다.")
    void whenUpdateMonthlyGoalStatusThenStatusShouldBeChanged() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.");
        final Long newMonthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        final MonthlyGoalStatusUpdateCommand statusUpdateCommand = new MonthlyGoalStatusUpdateCommand(true);
        monthlyGoalWriteUseCase.update(dailygeUser, newMonthlyGoalId, statusUpdateCommand);

        final MonthlyGoalJpaEntity findMonthlyGoal = monthlyGoalReadUseCase.findById(newMonthlyGoalId);
        assertTrue(findMonthlyGoal.isDone());
    }

    @Test
    @DisplayName("MonthlyGoal 내용을 업데이트하면, 내용이 변경된다.")
    void whenUpdateMonthlyGoalThenContentsShouldBeChanged() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.");
        final Long newMonthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);
        final String changedTitle = "마라톤 참여";
        final String changedContent = "가족들과 한강 마라톤 참여";
        final MonthlyGoalUpdateCommand statusUpdateCommand = new MonthlyGoalUpdateCommand(changedTitle, changedContent);
        monthlyGoalWriteUseCase.update(dailygeUser, newMonthlyGoalId, statusUpdateCommand);

        final MonthlyGoalJpaEntity findMonthlyGoal = monthlyGoalReadUseCase.findById(newMonthlyGoalId);
        assertAll(
            () -> assertEquals(changedTitle, findMonthlyGoal.getTitle()),
            () -> assertEquals(changedContent, findMonthlyGoal.getContent())
        );
    }

    @Test
    @DisplayName("MonthlyGoal 상태를 변경할 때, MonthlyGoal이 존재하지 않으면 MonthlyGoalTypeException이 발생한다.")
    void whenUpdateMonthlyGoalStatusButNotExistsThenMonthlyGoalTypeExceptionShouldBeHappen() {
        final Long invalidId = Long.MAX_VALUE;
        final MonthlyGoalStatusUpdateCommand statusUpdateCommand = new MonthlyGoalStatusUpdateCommand(true);

        assertThatThrownBy(() -> monthlyGoalWriteUseCase.update(dailygeUser, invalidId, statusUpdateCommand))
            .isInstanceOf(MonthlyGoalTypeException.class)
            .hasMessage(MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("MonthlyGoal을 변경할 때, MonthlyGoal이 존재하지 않으면 MonthlyGoalTypeException이 발생한다.")
    void whenUpdateMonthlyGoalButNotExistsThenMonthlyGoalTypeExceptionShouldBeHappen() {
        final Long invalidId = Long.MAX_VALUE;
        final String changedTitle = "마라톤 참여";
        final String changedContent = "가족들과 한강 마라톤 참여";
        final MonthlyGoalUpdateCommand statusUpdateCommand = new MonthlyGoalUpdateCommand(changedTitle, changedContent);

        assertThatThrownBy(() -> monthlyGoalWriteUseCase.update(dailygeUser, invalidId, statusUpdateCommand))
            .isInstanceOf(MonthlyGoalTypeException.class)
            .hasMessage(MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND.getMessage());
    }
}
