package Model;

public class AuthToken {
    private String token;
    private String username;
    private String personID;

    public AuthToken(String token, String username, String personID) {
        this.token = token;
        this.username = username;
        this.personID = personID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
