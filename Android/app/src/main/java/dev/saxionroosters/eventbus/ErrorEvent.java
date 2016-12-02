package dev.saxionroosters.eventbus;

/**
 * Created by jelle on 27/11/2016.
 *
 * Used whenever you want to push a Schedule through EventBus to a registered Eventbus listener.
 */
public class ErrorEvent {

    private int statusCode = -1;                    //default = -1 = unknown
    private String message = "unknown error";       //default = "unknown error"


    public ErrorEvent(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorEvent() {

    }

    public int getStatus() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
