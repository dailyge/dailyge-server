package project.dailyge.entity.applicationhistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static project.dailyge.entity.applicationhistory.ApplicationStatus.BEFORE_SUBMISSION;

public final class ApplicationHistoryFixture {

    private ApplicationHistoryFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final ApplicationHistoryJpaEntity newResumeSubmissionHistory = new ApplicationHistoryJpaEntity(
        "Dailyge 프론트엔드 개발자 채용 공고",
        "https://recruit.dailyge.com",
        BEFORE_SUBMISSION,
        LocalDate.of(2024, 11, 4),
        LocalDateTime.of(2024, 11, 4, 0, 0, 0),
        2024,
        11,
        1L,
        1L
    );
}
