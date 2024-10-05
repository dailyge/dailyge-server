package project.dailyge.app.test.anniversary.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.hibernate.boot.model.source.internal.hbm.XmlElementMetadata.ARRAY;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static project.dailyge.app.common.CommonSnippet.TOKEN_COOKIE_DESCRIPTORS;
import static project.dailyge.app.common.SnippetUtils.getAttribute;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryUpdateRequest;

public interface AnniversarySnippet {

    String TAG = "Anniversary";
    String identifier = "{class_name}/{method_name}/";

    FieldDescriptor[] ANNIVERSARY_CREATE_REQUEST_FIELDS = {
        fieldWithPath("name").description("기념일")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "name")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "date")),
        fieldWithPath("remind").description("리마인드"),
        fieldWithPath("emojiId").description("이모티콘 ID")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "emojiId")),
    };

    FieldDescriptor[] ANNIVERSARY_UPDATE_REQUEST_FIELDS = {
        fieldWithPath("name").description("기념일")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "name")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "date")),
        fieldWithPath("remind").description("리마인드"),
        fieldWithPath("emojiId").description("이모티콘 ID")
            .attributes(getAttribute(AnniversaryUpdateRequest.class, "emojiId")),
    };

    FieldDescriptor[] ANNIVERSARY_RESPONSE = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ANNIVERSARY_CREATE_RESPONSE = {
        fieldWithPath("data.anniversaryId").type(NUMBER).description("기념일 PK"),
        fieldWithPath("data").type(OBJECT).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ANNIVERSARIES_READ_RESPONSE = {
        fieldWithPath("data.[].anniversaryId").type(NUMBER).description("기념일 PK"),
        fieldWithPath("data.[].name").type(STRING).description("기념일"),
        fieldWithPath("data.[].date").type(STRING).description("날짜"),
        fieldWithPath("data").type(ARRAY).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] ANNIVERSARY_ID_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("anniversaryId").description("Anniversary ID")
    };

    ParameterDescriptor[] ANNIVERSARY_DATE_PARAMETER_DESCRIPTORS = {
        parameterWithName("startDate").optional().description("검색 시작 날짜 (형식: yyyy-MM-dd)"),
        parameterWithName("endDate").optional().description("검색 종료 날짜 (형식: yyyy-MM-dd)")
    };

    RequestCookiesSnippet ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(TOKEN_COOKIE_DESCRIPTORS);
    RequestFieldsSnippet ANNIVERSARY_CREATE_REQUEST_SNIPPET = requestFields(ANNIVERSARY_CREATE_REQUEST_FIELDS);
    RequestFieldsSnippet ANNIVERSARY_UPDATE_REQUEST_SNIPPET = requestFields(ANNIVERSARY_UPDATE_REQUEST_FIELDS);
    ResponseFieldsSnippet ANNIVERSARY_CREATE_RESPONSE_SNIPPET = responseFields(ANNIVERSARY_CREATE_RESPONSE);
    ResponseFieldsSnippet ANNIVERSARY_UPDATE_RESPONSE_SNIPPET = responseFields(ANNIVERSARY_RESPONSE);
    ResponseFieldsSnippet ANNIVERSARIES_READ_RESPONSE_SNIPPET = responseFields(ANNIVERSARIES_READ_RESPONSE);
    PathParametersSnippet ANNIVERSARY_PATH_PARAMETER_SNIPPET = pathParameters(ANNIVERSARY_ID_PATH_PARAMETER_DESCRIPTORS);
    QueryParametersSnippet ANNIVERSARY_DATE_QUERY_PARAMS = queryParameters(ANNIVERSARY_DATE_PARAMETER_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
