package pt.lsts.util;

/**
 * Created by zp on 28-05-2016.
 */
public class EventSystemBecameVisible extends AbstractEvent {

    protected final String system;

    public EventSystemBecameVisible(String system) {
        this.system = system;
    }

    public String getSystem() {
        return system;
    }
}
