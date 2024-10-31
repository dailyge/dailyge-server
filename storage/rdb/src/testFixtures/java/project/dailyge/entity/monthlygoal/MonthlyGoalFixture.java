package project.dailyge.entity.monthlygoal;

import project.dailyge.entity.goal.MonthlyGoalJpaEntity;

import java.time.LocalDate;

public final class MonthlyGoalFixture {

    private MonthlyGoalFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final MonthlyGoalJpaEntity newMonthlyGoal = new MonthlyGoalJpaEntity(
        1L,
        "달성 목표",
        "월간 목표 내용",
        false,
        LocalDate.now(),
        1L
    );
}
