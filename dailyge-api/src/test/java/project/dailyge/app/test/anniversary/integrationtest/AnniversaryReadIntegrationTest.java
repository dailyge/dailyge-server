package project.dailyge.app.test.anniversary.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.application.AnniversaryReadService;
import project.dailyge.app.core.anniversary.application.AnniversaryWriteService;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage;
import project.dailyge.app.core.anniversary.exception.AnniversaryTypeException;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDateTime;

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
    @DisplayName("기념일이 존재하지 않으면 AnniversaryTypeException이 발생한다..")
    void whenAnniversaryNotExistsThenAnniversaryTypeExceptionShouldBeHappen() {
        final Long invalidAnniversaryId = Long.MAX_VALUE;
        assertThatThrownBy(() -> anniversaryReadService.findById(invalidAnniversaryId))
            .isInstanceOf(AnniversaryTypeException.class)
            .hasMessage(AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND.message());
    }
}
