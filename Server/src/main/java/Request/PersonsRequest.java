package Request;

public class PersonsRequest {
    private String authToken;

    public PersonsRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getToken() {
        return authToken;
    }

    public void setToken(String authToken) {
        this.authToken = authToken;
    }
}
