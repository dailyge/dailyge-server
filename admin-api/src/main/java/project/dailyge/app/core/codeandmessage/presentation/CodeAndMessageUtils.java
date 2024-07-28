package project.dailyge.app.core.codeandmessage.presentation;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import project.dailyge.app.response.CodeAndMessageResponse;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CodeAndMessageUtils {

    private CodeAndMessageUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Map<String, List<CodeAndMessageResponse>> convertToMap(List<CodeAndMessageJpaEntity> findCodeAndMessages) {
        return findCodeAndMessages.stream()
            .collect(groupingBy(CodeAndMessageJpaEntity::getDomain, mapping(convertToResponse(), toList())));
    }

    private static Function<CodeAndMessageJpaEntity, CodeAndMessageResponse> convertToResponse() {
        return entity -> new CodeAndMessageResponse(
            entity.getId(),
            entity.getDomain(),
            entity.getName(),
            entity.getMessage(),
            entity.getCode()
        );
    }
}
