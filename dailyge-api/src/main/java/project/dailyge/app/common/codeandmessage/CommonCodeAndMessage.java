package project.dailyge.app.common.codeandmessage;

public enum CommonCodeAndMessage implements CodeAndMessage {
    OK(200, "OK"),
    CREATED(201, "Created");

    private final int code;
    private final String message;

    CommonCodeAndMessage(
        int code,
        String message
    ) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
