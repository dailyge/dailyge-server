package project.dailyge.entity.event;

public interface EventEntityWriteRepository {
    Long save(EventJpaEntity event);
}
