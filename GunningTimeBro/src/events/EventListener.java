package events;

public interface EventListener<T extends GameEvent> {
    void onEvent(T event);
}