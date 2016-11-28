package dev.saxionroosters.eventbus;

/**
 * Created by jelle on 27/11/2016.
 *
 * Used whenever you want to push a Schedule through EventBus to a registered Eventbus listener.
 */
public class ErrorEvent {

    private Error error;

    public ErrorEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
