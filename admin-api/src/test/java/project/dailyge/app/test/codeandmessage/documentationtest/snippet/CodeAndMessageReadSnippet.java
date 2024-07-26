package project.dailyge.app.test.codeandmessage.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import com.epages.restdocs.apispec.Schema;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class CodeAndMessageReadSnippet implements CodeAndMessageSnippet {

    private static final String SUMMARY = "응답 코드&메시지 목록 조회 API";
    private static final String DESCRIPTION = "Dailyge 서비스에서 사용중인 응답 코드&메시지 목록 조회 API 입니다.";

    private CodeAndMessageReadSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createMonthlyTasksFilter(
        final String identifier,
        final String responseSchema
    ) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .responseSchema(Schema.schema(responseSchema))
                .responseFields(CODE_AND_MESSAGE_RESPONSE_FILE_DESCRIPTOR)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    responseFields(Arrays.stream(CODE_AND_MESSAGE_RESPONSE_FILE_DESCRIPTOR).toList())
                );
            }
        );
    }
}
