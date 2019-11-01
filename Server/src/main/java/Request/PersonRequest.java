package Request;

public class PersonRequest {
    private String authToken;
    private String personID;

    public PersonRequest(String authToken, String personID) {
        this.authToken = authToken;
        this.personID = personID;
    }

    public String getToken() {
        return authToken;
    }

    public void setToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
