package project.dailyge.app.test.codeandmessage.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.response.CodeAndMessagesResponse;
import static project.dailyge.app.test.codeandmessage.documentationtest.snippet.CodeAndMessageReadSnippet.createMonthlyTasksFilter;
import static project.dailyge.app.test.codeandmessage.documentationtest.snippet.CodeAndMessageSnippet.CODE_AND_MESSAGE_RESPONSE_FIELD_SNIPPET;
import static project.dailyge.app.test.codeandmessage.documentationtest.snippet.CodeAndMessageSnippet.createIdentifier;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteService;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

import java.util.ArrayList;
import java.util.List;

@DisplayName("[DocumentationTest] 코드&메시지 조회 문서화 테스트")
class CodeAndMessageReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private CodeAndMessageEntityWriteService codeAndMessageWriteService;

    @Test
    @Disabled
    @DisplayName("[RestDocs] Dailyge 서비스에 사용된 응답 코드&메시지를 조회하면 200 OK를 받는다.")
    void whenSearchCodeAndMessageThenResultShouldBe_200_RestDocs() {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(createCodeAndMessages());
        codeAndMessageWriteService.saveAll(codeAndMessages);

        given(this.specification)
            .filter(document(IDENTIFIER, CODE_AND_MESSAGE_RESPONSE_FIELD_SNIPPET))
            .get("/api/codeandmessages")
            .then()
            .log()
            .all();
    }

    @Test
    @Disabled
    @DisplayName("[Swagger] Dailyge 서비스에 사용된 응답 코드&메시지를 조회하면 200 OK를 받는다.")
    void whenSearchCodeAndMessageThenResultShouldBe_200_Swagger() {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(createCodeAndMessages());
        codeAndMessageWriteService.saveAll(codeAndMessages);
        final RestDocumentationFilter filter = createMonthlyTasksFilter(
            createIdentifier("CodeAndMessageListRead", 200), CodeAndMessagesResponse.class.getSimpleName()
        );

        given(this.specification)
            .filter(filter)
            .get("/api/codeandmessages")
            .then()
            .log()
            .all();
    }

    private List<CodeAndMessageJpaEntity> createCodeAndMessages() {
        return new ArrayList<>();
    }
}
