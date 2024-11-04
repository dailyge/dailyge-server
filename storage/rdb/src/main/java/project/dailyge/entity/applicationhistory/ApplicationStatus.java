package project.dailyge.entity.applicationhistory;

public enum ApplicationStatus {
    BEFORE_SUBMISSION("제출 전"),
    SUBMITTED("제출 완료"),
    PREPARING_CODING_TEST("코딩테스트"),
    PREPARING_PERSONALITY_TEST("인적성검사"),
    FIRST_INTERVIEW("1차 면접"),
    SECOND_INTERVIEW("2차 면접"),
    THIRD_INTERVIEW("3차 면접"),
    FINAL_INTERVIEW("최종 면접"),
    CLOSED("공고 마감"),
    DOCUMENT_REJECTED("서류 탈락"),
    CODING_TEST_REJECTED("코딩테스트 탈락"),
    INTERVIEW_REJECTED("면접 탈락"),
    PERSONALITY_TEST_REJECTED("인적성검사 탈락");

    private final String kr;

    ApplicationStatus(final String kr) {
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }
}
