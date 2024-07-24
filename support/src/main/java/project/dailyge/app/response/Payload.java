package project.dailyge.app.response;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public class Payload<T> {

    private final int code;
    private final String message;
    private final T data;

    public Payload(
        final CodeAndMessage codeAndMessage,
        final T data
    ) {
        this.code = codeAndMessage.code();
        this.message = codeAndMessage.message();
        this.data = data;
    }
}
