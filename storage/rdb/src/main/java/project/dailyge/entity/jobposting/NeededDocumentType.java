package project.dailyge.entity.jobposting;

public enum NeededDocumentType {
    RESUME("이력서"),
    PORTFOLIO("포트폴리오"),
    PERSONAL_STATEMENT("자기소개서"),
    TECHNICAL_EXPERIENCE("경력기술서"),
    MOTIVATION_TO_APPLY("지원동기"),
    GITHUB("깃허브"),
    BLOG("블로그");

    private final String kr;

    NeededDocumentType(final String kr) {
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }
}
