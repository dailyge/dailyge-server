package project.dailyge.entity.jobposting;

public enum JobPostingStatus {
    SEARCHING("공고 서치"),
    PREPARING_DOCUMENTS("서류 준비 중"),
    SUBMITTED("제출 완료"),
    PREPARING_CODING_TEST("코딩테스트 준비 중"),
    PREPARING_PERSONALITY_TEST("인적성 준비 중"),
    FIRST_INTERVIEW("1차 면접"),
    SECOND_INTERVIEW("2차 면접"),
    THIRD_INTERVIEW("3차 면접"),
    FINAL_INTERVIEW("최종 면접"),
    CLOSED("공고 마감"),
    DOCUMENT_REJECTED("서류 탈락"),
    CODING_TEST_REJECTED("코딩테스트 탈락"),
    PERSONALITY_TEST_REJECTED("인적성 탈락");

    private final String kr;

    JobPostingStatus(final String kr) {
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }
}
