package project.dailyge.app.core.codeandmessage;

import org.springframework.stereotype.Component;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.codeandmessage.CodeAndMessagesFactory;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.core.task.exception.TaskCodeAndMessage;
import project.dailyge.app.core.user.exception.UserCodeAndMessage;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class CodeAndMessageConfig {

    private static final String COMMON = "common";
    private static final String TASKS = "tasks";
    private static final String USERS = "users";

    private final CodeAndMessagesFactory factory = initCodeAndMessages();

    private CodeAndMessagesFactory initCodeAndMessages() {
        final CodeAndMessagesFactory codeAndMessagesFactory = new CodeAndMessagesFactory();
        codeAndMessagesFactory.put(COMMON, CommonCodeAndMessage.class);
        codeAndMessagesFactory.put(TASKS, TaskCodeAndMessage.class);
        codeAndMessagesFactory.put(USERS, UserCodeAndMessage.class);
        return codeAndMessagesFactory;
    }

    public List<CodeAndMessageJpaEntity> getAllCodeAndMessages() {
        final List<CodeAndMessageJpaEntity> entities = new ArrayList<>();
        entities.addAll(convert(COMMON));
        entities.addAll(convert(USERS));
        entities.addAll(convert(TASKS));
        return entities;
    }

    private List<CodeAndMessageJpaEntity> convert(final String domain) {
        final List<CodeAndMessage> findCodeAndMessages = factory.getCodeAndMessages(domain);
        final List<CodeAndMessageJpaEntity> entities = new ArrayList<>();
        for (final CodeAndMessage codeAndMessage : findCodeAndMessages) {
            final CodeAndMessageJpaEntity entity = createCodeAndMessage(domain, codeAndMessage);
            entities.add(entity);
        }
        return entities;
    }

    private CodeAndMessageJpaEntity createCodeAndMessage(
        final String domain,
        final CodeAndMessage codeAndMessage
    ) {
        return CodeAndMessageJpaEntity.builder()
            .domain(domain)
            .name(codeAndMessage.toString())
            .code(codeAndMessage.code())
            .message(codeAndMessage.message())
            .build();
    }
}
