package pt.lsts.util;

/**
 * Created by zp on 29-05-2016.
 */
public class EventMainSystemChanged extends AbstractEvent {

    protected final String current, previous;

    public EventMainSystemChanged(String previous, String current) {
        this.current = current;
        this.previous = previous;
    }

    public String getCurrent() {
        return current;
    }

    public String getPrevious() {
        return previous;
    }
}
