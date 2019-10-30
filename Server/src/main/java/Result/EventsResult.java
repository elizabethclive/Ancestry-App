package Result;

import java.util.ArrayList;

import Model.Event;

public class EventsResult {
    private ArrayList<Event> events;

    public EventsResult(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
