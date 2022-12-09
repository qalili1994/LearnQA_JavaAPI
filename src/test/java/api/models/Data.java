package api.models;

public class Data {

    private String message;
    private String timestamp;

    public Data() {
    }

    public Data(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
