package project.dailyge.entity.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class Event {

    protected Long publisher;
    protected String domain;
    protected String eventId;
    protected EventType eventType;
    protected int publishCount;
    protected LocalDateTime createdAt;
}
