package project.dailyge.entity.task;

public enum TaskStatus {
    TODO("할 일"),
    IN_PROGRESS("진행 중"),
    DONE("완료");

    private final String kr;

    TaskStatus(final String kr) {
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }
}
