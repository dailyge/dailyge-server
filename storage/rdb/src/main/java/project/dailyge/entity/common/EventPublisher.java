package project.dailyge.entity.common;

public interface EventPublisher<T> {
    void publishExternalEvent(T event);
}
