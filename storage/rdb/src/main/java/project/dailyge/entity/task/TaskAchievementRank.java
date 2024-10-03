package project.dailyge.entity.task;

public enum TaskAchievementRank {
    A(0, 80.0),
    B(1, 60.0),
    C(2, 40.0),
    D(3, 20.0),
    F(4, 0.0);

    private final int rank;
    private final double minAchievementRate;

    TaskAchievementRank(
        final int rank,
        final double minAchievementRate
    ) {
        this.rank = rank;
        this.minAchievementRate = minAchievementRate;
    }

    public static int getAchievementRank(final double achievementRate) {
        for (final TaskAchievementRank rank : TaskAchievementRank.values()) {
            if (achievementRate >= rank.minAchievementRate) {
                return rank.rank;
            }
        }
        return F.rank;
    }
}
