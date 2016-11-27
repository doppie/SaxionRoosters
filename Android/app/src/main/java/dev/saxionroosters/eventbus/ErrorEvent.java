package dev.saxionroosters.eventbus;

/**
 * Created by jelle on 27/11/2016.
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
