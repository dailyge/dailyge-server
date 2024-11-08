package project.dailyge.app.core.note.application.usecase;

import org.springframework.stereotype.Component;

@Component
public class NoteValidator {

    public void validate(
        final Long senderId,
        final Long receiverId
    ) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("받은 사람이 올바르지 않습니다.");
        }
    }
}
