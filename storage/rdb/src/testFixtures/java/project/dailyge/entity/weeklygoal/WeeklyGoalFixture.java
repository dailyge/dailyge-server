package project.dailyge.entity.weeklygoal;

import java.time.LocalDateTime;

public class WeeklyGoalFixture {

    private WeeklyGoalFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final WeeklyGoalJpaEntity newWeeklyGoal = new WeeklyGoalJpaEntity(
        1L,
        "달성 주간 목표",
        "주간 목표 내용",
        LocalDateTime.now(),
        1L
    );
}
