package Util;

//public class EventsResult extends Result {
//
//    public EventsResult(boolean success, String message) {
//        super(success, message);
//    }
//}

import Model.Event;

public class EventsResult {
    private Event[] data;
    String message;

    public EventsResult(String message) {
        this.message = message;
    }

    public EventsResult(Event[] data) {
        this.data = data;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}