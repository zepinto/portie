package pt.lsts.util;

/**
 * Created by zp on 28-05-2016.
 */
public abstract class AbstractEvent {

    protected final long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }
}
