package Request;

public class FillRequest {
    private String userName;
    private int generations;

    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
