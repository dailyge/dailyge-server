package project.dailyge.app.test.note.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;
import static project.dailyge.app.common.CommonSnippet.ERROR_RESPONSE;
import static project.dailyge.app.common.CommonSnippet.TOKEN_COOKIE_DESCRIPTORS;

import java.util.Arrays;
import java.util.List;

public final class NoteCreateSnippet implements NoteSnippet {

    private static final String SUMMARY = "쪽지 전송 API";
    private static final String DESCRIPTION = "쪽지를 전송합니다.";

    private NoteCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createNoteFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .requestFields(NoteSnippet.Companion.getNOTE_CREATE_REQUEST_FIELDS())
                .responseFields(NoteSnippet.Companion.getNOTE_CREATE_RESPONSE_FIELDS())
                .tag(TAG)
                .summary(SUMMARY)
                .description(DESCRIPTION)
                .privateResource(false)
                .deprecated(false),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(NoteSnippet.Companion.getNOTE_CREATE_REQUEST_FIELDS().stream().toList()),
                    responseFields(NoteSnippet.Companion.getNOTE_CREATE_RESPONSE_FIELDS().stream().toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
