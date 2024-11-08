package project.dailyge.app.test.note.fixture;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;

import java.time.LocalDateTime;

public final class NoteCommandFixture {

    private NoteCommandFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static NoteCreateCommand createNoteCommand(
        final DailygeUser dailygeUser,
        final LocalDateTime sentAt
    ) {
        return new NoteCreateCommand(
            "커피챗 신청합니다.", "2024년 10월 11일 13:00", sentAt, "kmularise", dailygeUser.getId()
        );
    }
}
