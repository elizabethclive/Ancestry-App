package Result;

public class Result {
    private transient boolean success;
    private String message;

    Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return message;
    }

    public void setResult(String message) {
        this.message = message;
    }
}
