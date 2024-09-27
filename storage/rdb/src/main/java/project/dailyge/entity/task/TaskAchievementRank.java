package project.dailyge.entity.task;

public enum TaskAchievementRank {
    A(0, 80.0),
    B(1, 60.0),
    C(2, 40.0),
    D(3, 20.0),
    F(4, 0.0);

    private int rankNumber;
    private double minAchievementRate;

    TaskAchievementRank(
        final int rankNumber,
        final double minAchievementRate
    ) {
        this.rankNumber = rankNumber;
        this.minAchievementRate = minAchievementRate;
    }

    public static int getAchievementRank(final double achievementRate) {
        for (TaskAchievementRank rank : TaskAchievementRank.values()) {
            if (achievementRate >= rank.minAchievementRate) {
                return rank.rankNumber;
            }
        }
        return F.rankNumber;
    }
}
