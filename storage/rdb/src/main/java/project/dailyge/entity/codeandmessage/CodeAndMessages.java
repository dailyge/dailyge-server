package project.dailyge.entity.codeandmessage;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class CodeAndMessages {

    private static final String DELIMITER = ":";
    private final List<CodeAndMessageJpaEntity> codeAndMessages;

    public CodeAndMessages(final List<CodeAndMessageJpaEntity> codeAndMessages) {
        if (codeAndMessages == null || codeAndMessages.isEmpty()) {
            this.codeAndMessages = new ArrayList<>();
            return;
        }
        this.codeAndMessages = codeAndMessages;
    }

    public Map<String, CodeAndMessageJpaEntity> convertToMap(final List<CodeAndMessageJpaEntity> codeAndMessages) {
        final Map<String, CodeAndMessageJpaEntity> map = new HashMap<>();
        for (final CodeAndMessageJpaEntity entity : codeAndMessages) {
            map.put(getKey(entity), entity);
        }
        return map;
    }

    private String getKey(final CodeAndMessageJpaEntity entity) {
        return entity.getDomain() + DELIMITER + entity.getName();
    }

    public void updateAll(final List<CodeAndMessageJpaEntity> codeAndMessages) {
        if (this.codeAndMessages.isEmpty()) {
            this.codeAndMessages.addAll(codeAndMessages);
            return;
        }
        final Map<String, CodeAndMessageJpaEntity> sourceMap = convertToMap(this.codeAndMessages);
        final Map<String, CodeAndMessageJpaEntity> targetMap = convertToMap(codeAndMessages);
        for (final String targetKey : targetMap.keySet()) {
            update(sourceMap.get(targetKey), targetMap.get(targetKey));
        }
    }

    private void update(
        final CodeAndMessageJpaEntity source,
        final CodeAndMessageJpaEntity target
    ) {
        source.update(target);
    }
}
