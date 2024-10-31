package project.dailyge.app.core.event.persistence;

import project.dailyge.entity.event.EventJpaEntity;

import java.time.LocalDateTime;

public class EventCache {

    private final Long id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long participants;

    public EventCache(
        final Long id,
        final LocalDateTime startTime,
        final LocalDateTime endTime,
        final Long participants
    ) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Long getParticipants() {
        return participants;
    }

    public static EventCache entityToCache(final EventJpaEntity eventJpaEntity) {
        return new EventCache(
            eventJpaEntity.getId(),
            eventJpaEntity.getStartDate(),
            eventJpaEntity.getEndDate(),
            eventJpaEntity.getParticipants()
        );
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventCache that)) {
            return false;
        }
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getStartTime() != null ? !getStartTime().equals(that.getStartTime()) : that.getStartTime() != null) {
            return false;
        }
        if (getEndTime() != null ? !getEndTime().equals(that.getEndTime()) : that.getEndTime() != null) {
            return false;
        }
        return getParticipants() != null ? getParticipants().equals(that.getParticipants()) : that.getParticipants() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
        result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
        result = 31 * result + (getParticipants() != null ? getParticipants().hashCode() : 0);
        return result;
    }
}
