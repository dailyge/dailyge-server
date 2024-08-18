package project.dailyge.app.test.notice.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.notice.application.NoticeWriteUseCase;
import project.dailyge.app.core.notice.application.command.NoticeCreateCommand;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static project.dailyge.entity.notice.NoticeType.UPDATE;

@DisplayName("[IntegrationTest] Notice 작성 통합 테스트")
class NoticeCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private NoticeWriteUseCase noticeWriteUseCase;

    @Test
    @DisplayName("공지사항을 등록하면, Id가 NULL이 아니다.")
    void whenCreateNoticeThenIdShouldBeNotNull() {
        NoticeCreateCommand noticeCreateCommand = new NoticeCreateCommand(
            "0.1v 업데이트 사항",
            "이번에 새로운 기능을 도입하였습니다.",
            UPDATE,
            1L
        );
        assertNotNull(noticeWriteUseCase.save(noticeCreateCommand));
    }
}
