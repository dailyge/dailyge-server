package project.dailyge.entity.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class Event {

    protected static final String INVALID_PUBLISHER_ID_ERROR_MESSAGE = "올바른 Publisher Id를 입력해주세요.";
    protected static final String INVALID_EVENT_ID_ERROR_MESSAGE = "올바른 Event Id를 입력해주세요.";
    protected static final String INVALID_EVENT_TYPE_ERROR_MESSAGE = "올바른 Event Type을 입력해주세요.";

    protected Long publisher;
    protected String domain;
    protected String eventId;
    protected EventType eventType;
    protected int publishCount;
    protected LocalDateTime createdAt;
}
