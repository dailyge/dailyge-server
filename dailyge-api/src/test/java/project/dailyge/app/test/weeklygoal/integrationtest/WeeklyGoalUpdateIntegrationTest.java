package project.dailyge.app.test.weeklygoal.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;
import project.dailyge.app.core.weeklygoal.exception.WeeklyGoalCodeAndMessage;
import project.dailyge.app.core.weeklygoal.exception.WeeklyGoalTypeException;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityReadRepository;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 주간 목표 수정 통합 테스트")
class WeeklyGoalUpdateIntegrationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 수정 API 개발";
    private static final String DEFAULT_CONTENT = "주간 목표 수정 API 개발";
    private static final String CHANGED_TITLE = "운동하기";
    private static final String CHANGED_CONTENT = "러닝 30분하기";

    @Autowired
    private WeeklyGoalEntityReadRepository weeklyGoalEntityReadRepository;

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("WeeklyGoal 완료 상태를 업데이트하면, 상태가 변경된다.")
    void whenUpdateWeeklyGoalStatusThenStatusShouldBeChanged() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        weeklyGoalWriteService.update(dailygeUser, newWeeklyGoalId, true);

        WeeklyGoalJpaEntity findWeeklyGoal = weeklyGoalEntityReadRepository.findById(newWeeklyGoalId).orElseThrow();
        assertTrue(findWeeklyGoal.isDone());
    }

    @Test
    @DisplayName("유효하지 않은 사용자가 WeeklyGoal 완료 상태를 업데이트하면, CommonException을 던진다.")
    void whenUpdateWeeklyGoalStatusByInvalidUserThenCommonExceptionShouldBeThrown() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);

        assertThatThrownBy(() -> weeklyGoalWriteService.update(invalidUser, newWeeklyGoalId, true))
            .isInstanceOf(CommonException.class)
            .hasMessage(CommonCodeAndMessage.INVALID_USER_ID.message());
    }

    @Test
    @DisplayName("WeeklyGoal 내용을 업데이트하면, 내용이 변경된다.")
    void whenUpdateWeeklyGoalThenContentsShouldBeChanged() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalUpdateCommand statusUpdateCommand = new WeeklyGoalUpdateCommand(CHANGED_TITLE, CHANGED_CONTENT);
        weeklyGoalWriteService.update(dailygeUser, newWeeklyGoalId, statusUpdateCommand);

        final WeeklyGoalJpaEntity findWeeklyGoal = weeklyGoalEntityReadRepository.findById(newWeeklyGoalId).orElseThrow();
        assertAll(
            () -> assertEquals(statusUpdateCommand.title(), findWeeklyGoal.getTitle()),
            () -> assertEquals(statusUpdateCommand.content(), findWeeklyGoal.getContent())
        );
    }

    @Test
    @DisplayName("WeeklyGoal 내용을 유효하지 않은 사용자가 업데이트하면, CommonException을 던진다.")
    void whenUpdateWeeklyGoalByInvalidUserThenCommonExceptionShouldBeThrown() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalUpdateCommand statusUpdateCommand = new WeeklyGoalUpdateCommand(CHANGED_TITLE, CHANGED_CONTENT);

        assertThatThrownBy(() -> weeklyGoalWriteService.update(invalidUser, newWeeklyGoalId, statusUpdateCommand))
            .isInstanceOf(CommonException.class)
            .hasMessage(CommonCodeAndMessage.INVALID_USER_ID.message());
    }

    @Test
    @DisplayName("WeeklyGoal 완료 상태를 변경할 때, WeeklyGoal이 존재하지 않으면 WeeklyGoalTypeException이 발생한다.")
    void whenUpdateWeeklyGoalDoneButNotExistsThenWeeklyGoalTypeExceptionShouldBeHappen() {
        final Long invalidId = Long.MAX_VALUE;
        assertThatThrownBy(() -> weeklyGoalWriteService.update(dailygeUser, invalidId, true))
            .isInstanceOf(WeeklyGoalTypeException.class)
            .hasMessage(WeeklyGoalCodeAndMessage.WEEKLY_GOAL_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("WeeklyGoal을 변경할 때, WeeklyGoal이 존재하지 않으면 WeeklyGoalTypeException이 발생한다.")
    void whenUpdateWeeklyGoalButNotExistsThenWeeklyGoalTypeExceptionShouldBeHappen() {
        final Long invalidId = Long.MAX_VALUE;
        final WeeklyGoalUpdateCommand statusUpdateCommand = new WeeklyGoalUpdateCommand(CHANGED_TITLE, CHANGED_CONTENT);

        assertThatThrownBy(() -> weeklyGoalWriteService.update(dailygeUser, invalidId, statusUpdateCommand))
            .isInstanceOf(WeeklyGoalTypeException.class)
            .hasMessage(WeeklyGoalCodeAndMessage.WEEKLY_GOAL_NOT_FOUND.getMessage());
    }
}
