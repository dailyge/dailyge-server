package project.dailyge.app.test.weeklygoal.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.entity.weeklygoal.WeeklyGoalEntityReadRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@DisplayName("[IntegrationTest] 주간 목표 삭제 통합 테스트")
class WeeklyGoalDeleteIntegrationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 삭제 API 개발";
    private static final String DEFAULT_CONTENT = "원격 저장소 올리기 전 셀프 리뷰";

    @Autowired
    private WeeklyGoalEntityReadRepository weeklyGoalEntityReadRepository;

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("삭제된 주간 목표를 조회하면 해당 주간 목표를 찾을 수 없다.")
    void whenSearchDeletedWeeklyGoalThenWeeklyGoalShouldNotBeFound() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        weeklyGoalWriteService.delete(dailygeUser, newWeeklyGoalId);

        assertTrue(weeklyGoalEntityReadRepository.findById(newWeeklyGoalId).isEmpty());
    }

    @Test
    @DisplayName("권한이 없는 사용자가 주간 목표를 삭제하려고 하면 CommonException을 던진다.")
    void whenDeleteByUnAuthorizedUserThenWeeklyGoalShouldThrowCommonException() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long newWeeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        assertThatThrownBy(() -> weeklyGoalWriteService.delete(invalidUser, newWeeklyGoalId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }
}
