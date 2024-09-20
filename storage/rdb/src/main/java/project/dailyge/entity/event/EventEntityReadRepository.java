package project.dailyge.entity.event;

public interface EventEntityReadRepository {
    EventJpaEntity findById(Long eventId);
}
