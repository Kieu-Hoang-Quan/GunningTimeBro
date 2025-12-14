package events;

import java.util.*;

public class EventBus {

    private static EventBus instance;
    private Map<Class<? >, List<EventListener<?>>> listeners;

    private EventBus() {
        listeners = new HashMap<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public <T extends GameEvent> void subscribe(Class<T> eventType, EventListener<T> listener) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new ArrayList<>());
        }
        listeners.get(eventType).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void publish(T event) {
        List<EventListener<? >> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }

    public void clear() {
        listeners.clear();
    }
}