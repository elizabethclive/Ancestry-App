package Util;

public class EventsRequest {
    private String authToken;

    public EventsRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getToken() {
        return authToken;
    }

    public void setToken(String authToken) {
        this.authToken = authToken;
    }
}
