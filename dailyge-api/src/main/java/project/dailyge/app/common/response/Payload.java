package project.dailyge.app.common.response;

import lombok.*;
import project.dailyge.app.common.codeandmessage.*;

@Getter
public class Payload<T> {

    private final int code;
    private final String message;
    private final T data;

    public Payload(
        CodeAndMessage codeAndMessage,
        T data
    ) {
        this.code = codeAndMessage.code();
        this.message = codeAndMessage.message();
        this.data = data;
    }
}
