package project.dailyge.app.codeandmessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeAndMessagesFactory {

    private final Map<String, List<CodeAndMessage>> codeAndMessages = new HashMap<>();

    public void put(
        final String key,
        final Class<? extends CodeAndMessage> clazz
    ) {
        final List<CodeAndMessage> codeAndMessages = Arrays.stream(clazz.getEnumConstants())
            .collect(Collectors.toList());
        this.codeAndMessages.put(key, codeAndMessages);
    }

    public List<CodeAndMessage> getCodeAndMessages(final String key) {
        return codeAndMessages.get(key);
    }
}
