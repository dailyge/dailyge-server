package project.dailyge.app.response;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public class Payload<T> {

    protected final int code;
    protected final String message;
    protected final T data;

    public Payload(
        final CodeAndMessage codeAndMessage,
        final T data
    ) {
        this.code = codeAndMessage.code();
        this.message = codeAndMessage.message();
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("{\"code\":%d, \"message\":\"%s\", \"data\":%s}", code, message, data);
    }
}
