package events;

public abstract class GameEvent {

    private long timestamp;
    private Object source;

    public GameEvent(Object source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    public Object getSource() { return source; }
    public long getTimestamp() { return timestamp; }
    public abstract String getEventType();
}