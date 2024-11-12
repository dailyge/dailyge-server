package project.dailyge.app.test.note.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;
import static project.dailyge.app.common.CommonSnippet.ERROR_RESPONSE;
import static project.dailyge.app.common.CommonSnippet.TOKEN_COOKIE_DESCRIPTORS;

import java.util.Arrays;
import java.util.List;

public final class NoteReadSnippet implements NoteSnippet {

    private static final String NOTE_DETAIL_SUMMARY = "쪽지 읽기 API";
    private static final String NOTE_DETAIL_DESCRIPTION = "쪽지를 상세 조회합니다.";

    private NoteReadSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createReceivedNoteDetailReadFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .pathParameters(Companion.getNOTE_ID_PATH_PARAMETER_DESCRIPTORS())
                .responseFields(Companion.getSENT_NOTE_DETAIL_READ_RESPONSE_FIELDS())
                .tag(TAG)
                .summary(NOTE_DETAIL_SUMMARY)
                .description(NOTE_DETAIL_DESCRIPTION)
                .privateResource(false)
                .deprecated(false),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Companion.getNOTE_ID_PATH_PARAMETER_DESCRIPTORS()),
                    responseFields(Companion.getSENT_NOTE_DETAIL_READ_RESPONSE_FIELDS().stream().toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createSentNotesReadFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .queryParameters(Companion.getNOTES_REQUEST_PARAMETERS_DESCRIPTOR())
                .responseFields(Companion.getSENT_NOTES_RESPONSE_FIELDS())
                .tag(TAG)
                .summary(NOTE_DETAIL_SUMMARY)
                .description(NOTE_DETAIL_DESCRIPTION)
                .privateResource(false)
                .deprecated(false),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TOKEN_COOKIE_DESCRIPTORS),
                    queryParameters(Companion.getNOTES_REQUEST_PARAMETERS_DESCRIPTOR()),
                    responseFields(Companion.getSENT_NOTES_RESPONSE_FIELDS().stream().toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createReceivedNotesReadFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .queryParameters(Companion.getNOTES_REQUEST_PARAMETERS_DESCRIPTOR())
                .responseFields(Companion.getRECEIVED_NOTES_RESPONSE_FIELDS())
                .tag(TAG)
                .summary(NOTE_DETAIL_SUMMARY)
                .description(NOTE_DETAIL_DESCRIPTION)
                .privateResource(false)
                .deprecated(false),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TOKEN_COOKIE_DESCRIPTORS),
                    queryParameters(Companion.getNOTES_REQUEST_PARAMETERS_DESCRIPTOR()),
                    responseFields(Companion.getRECEIVED_NOTES_RESPONSE_FIELDS().stream().toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
