package project.dailyge.entity.common;

public interface EventPublisher<T> {
    void publishInternalEvent(T event);
}
