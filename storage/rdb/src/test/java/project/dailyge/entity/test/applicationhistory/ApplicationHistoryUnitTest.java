package project.dailyge.entity.test.applicationhistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.applicationhistory.ApplicationHistoryFixture;
import project.dailyge.entity.applicationhistory.ApplicationHistoryJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.entity.applicationhistory.ApplicationStatus.BEFORE_SUBMISSION;

@DisplayName("[UnitTest] ApplicationHistory 단위 테스트")
public class ApplicationHistoryUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 제출 이력이 생성된다.")
    void whenValidArgumentsProvidedThenApplicationHistoryIsCreated() {
        final ApplicationHistoryJpaEntity applicationHistory = ApplicationHistoryFixture.newResumeSubmissionHistory;

        assertAll(
            () -> assertEquals("Dailyge 프론트엔드 개발자 채용 공고", applicationHistory.getTitle()),
            () -> assertEquals("https://recruit.dailyge.com", applicationHistory.getLink()),
            () -> assertEquals(BEFORE_SUBMISSION, applicationHistory.getStatus()),
            () -> assertEquals(LocalDate.of(2024, 11, 4), applicationHistory.getSubmittedDate()),
            () -> assertEquals(LocalDateTime.of(2024, 11, 4, 0, 0, 0), applicationHistory.getDeadline()),
            () -> assertEquals(1L, applicationHistory.getCompanyId()),
            () -> assertEquals(1L, applicationHistory.getUserId())
        );
    }
}
