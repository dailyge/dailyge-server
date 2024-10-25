package project.dailyge.app.test.anniversary.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DisplayName("[IntegrationTest] 기념일 조회 통합 테스트")
class AnniversaryReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryWriteService anniversaryWriteService;

    @Autowired
    private AnniversaryReadService anniversaryReadService;

    @Test
    @DisplayName("기념일이 존재하면 ID로 찾을 수 있다.")
    void whenAnniversaryExistsThenCanFindById() {
        final LocalDateTime now = LocalDateTime.now();
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        final Long newAnniversaryId = anniversaryWriteService.save(dailygeUser, command);

        final AnniversaryJpaEntity findAnniversary = anniversaryReadService.findById(newAnniversaryId);
        assertNotNull(findAnniversary);
    }

    @Test
    @DisplayName("기념일이 존재하지 않으면 AnniversaryTypeException이 발생한다.")
    void whenAnniversaryNotExistsThenAnniversaryTypeExceptionShouldBeHappen() {
        final Long invalidAnniversaryId = Long.MAX_VALUE;
        assertThatThrownBy(() -> anniversaryReadService.findById(invalidAnniversaryId))
            .isInstanceOf(AnniversaryTypeException.class)
            .hasMessage(ANNIVERSARY_NOT_FOUND.message());
    }

    @Test
    @DisplayName("기념일이 기간 내에 존재하면 결과가 반환된다.")
    void whenAnniversariesExistsBetweenDatesThenResultShouldNotBeEmpty() {
        final LocalDateTime now = LocalDateTime.now();
        final String name = "부모님 결혼 기념일";
        final boolean remind = false;
        final AnniversaryCreateCommand command = new AnniversaryCreateCommand(name, now, remind, null);
        anniversaryWriteService.save(dailygeUser, command);

        final LocalDate startDate = now.toLocalDate();
        final LocalDate endDate = startDate.plusDays(30);
        final List<AnniversaryJpaEntity> findAnniversaries = anniversaryReadService.findByDates(dailygeUser, startDate, endDate);
        assertFalse(findAnniversaries.isEmpty());
    }

    @Test
    @DisplayName("기념일이 기간 내에 존재하지 않으면 빈 리스트가 반환된다.")
    void whenAnniversariesNotExistsBetweenDatesThenResultShouldNotBeEmpty() {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDate startDate = now.toLocalDate();
        final LocalDate endDate = startDate.plusDays(30);
        final List<AnniversaryJpaEntity> findAnniversaries = anniversaryReadService.findByDates(dailygeUser, startDate, endDate);
        assertTrue(findAnniversaries.isEmpty());

        final List<AnniversaryJpaEntity> findReverseAnniversaries = anniversaryReadService.findByDates(dailygeUser, endDate, startDate);
        assertTrue(findReverseAnniversaries.isEmpty());
    }
}
