package project.dailyge.entity.test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.entity.task.TaskAchievementRank.getAchievementRank;

@DisplayName("[UnitTest] TaskAchievementRank 단위 테스트")
class TaskAchievementRankUnitTest {

    @ParameterizedTest
    @DisplayName("Task 달성 비율에 따라 알맞는 Rank의 값이 반환된다.")
    @CsvSource({"100.0, 0", "88.3, 0", "62.1, 1", "45.5, 2", "29.8, 3", "7.3, 4"})
    void whenFindRankByAchievementRateThenResultShouldBeCorrectly(
        final double achievementRate,
        final int rank
    ) {
        final int achievementRank = getAchievementRank(achievementRate);

        assertEquals(rank, achievementRank);
    }
}
