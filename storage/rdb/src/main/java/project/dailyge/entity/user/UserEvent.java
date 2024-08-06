package project.dailyge.entity.user;

import lombok.Getter;
import project.dailyge.entity.common.Event;
import project.dailyge.entity.common.EventType;

import static java.time.LocalDateTime.now;

@Getter
public class UserEvent extends Event {

    private static final String INVALID_PUBLISHER_ID_ERROR_MESSAGE = "올바른 Publisher Id를 입력해주세요.";
    private static final String INVALID_EVENT_ID_ERROR_MESSAGE = "올바른 Event Id를 입력해주세요.";
    private static final String INVALID_EVENT_TYPE_ERROR_MESSAGE = "올바른 Event Type을 입력해주세요.";

    private String nickname;
    private String email;
    private String profileImageUrl;

    /**
     * 직렬화를 위한 생성자로 외부에서 호출하지 말 것.
     */
    private UserEvent() {
    }

    private UserEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType,
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        validate(publisher, eventId, eventType);
        this.publisher = publisher;
        this.domain = domain;
        this.eventId = eventId;
        this.eventType = eventType;
        this.createdAt = now();
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserEvent createEvent(
        final Long publisher,
        final String eventId,
        final EventType eventType,
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        return new UserEvent(publisher, "user", eventId, eventType, nickname, email, profileImageUrl);
    }

    public void validate(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        if (publisher == null) {
            throw new IllegalArgumentException(INVALID_PUBLISHER_ID_ERROR_MESSAGE);
        }
        if (eventId == null) {
            throw new IllegalArgumentException(INVALID_EVENT_ID_ERROR_MESSAGE);
        }
        if (eventType == null) {
            throw new IllegalArgumentException(INVALID_EVENT_TYPE_ERROR_MESSAGE);
        }
    }

    public String getInvalidPublisherIdErrorMessage() {
        return INVALID_PUBLISHER_ID_ERROR_MESSAGE;
    }

    public String getInvalidEventIdErrorMessage() {
        return INVALID_EVENT_ID_ERROR_MESSAGE;
    }

    public String getInvalidEventTypeErrorMessage() {
        return INVALID_EVENT_TYPE_ERROR_MESSAGE;
    }

    @Override
    public String toString() {
        return "{"
            + "\"publisher\":\"" + publisher + "\","
            + "\"eventId\":\"" + eventId + "\","
            + "\"eventType\":\"" + eventType + "\","
            + "\"nickname\":\"" + nickname + "\","
            + "\"email\":\"" + email + "\","
            + "\"profileImageUrl\":\"" + profileImageUrl + "\","
            + "\"domain\":\"" + domain + "\","
            + "\"createdAt\":\"" + createdAt + "\""
            + "}";
    }
}
