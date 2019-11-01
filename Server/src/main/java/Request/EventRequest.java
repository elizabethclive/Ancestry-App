package Request;

public class EventRequest {
    private String authToken;
    private String eventID;

    public EventRequest(String authToken, String eventID) {
        this.authToken = authToken;
        this.eventID = eventID;
    }

    public String getToken() {
        return authToken;
    }

    public void setToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
