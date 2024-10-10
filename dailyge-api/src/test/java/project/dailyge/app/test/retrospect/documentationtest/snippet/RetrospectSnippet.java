package project.dailyge.app.test.retrospect.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectUpdateRequest;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.BOOLEAN;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface RetrospectSnippet {

    String TAG = "Retrospect";
    String identifier = "{class_name}/{method_name}/";

    CookieDescriptor[] TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    ParameterDescriptor[] RETROSPECT_ID_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("retrospectId").description("Retrospect ID")
    };

    ParameterDescriptor[] RETROSPECT_PAGING_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("page").description("페이지 번호").optional(),
        parameterWithName("limit").description("최대 개수").optional(),
    };

    FieldDescriptor[] RETROSPECT_CREATE_REQUEST_FIELDS = {
        fieldWithPath("title").description("회고 제목")
            .attributes(getAttribute(RetrospectCreateRequest.class, "title")),
        fieldWithPath("content").description("회고 내용")
            .attributes(getAttribute(RetrospectCreateRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(RetrospectCreateRequest.class, "date")),
        fieldWithPath("isPublic").description("공개 여부")
            .attributes(getAttribute(RetrospectCreateRequest.class, "isPublic")),
    };

    FieldDescriptor[] RETROSPECT_CREATE_RESPONSE_FIELDS = {
        fieldWithPath("data").type(OBJECT).description("데이터"),
        fieldWithPath("data.retrospectId").type(NUMBER).description("회고 ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] RETROSPECT_UPDATE_REQUEST_FIELDS = {
        fieldWithPath("title").description("회고 제목")
            .attributes(getAttribute(RetrospectUpdateRequest.class, "title")),
        fieldWithPath("content").description("회고 내용")
            .attributes(getAttribute(RetrospectUpdateRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(RetrospectUpdateRequest.class, "date")),
        fieldWithPath("isPublic").description("공개 여부")
            .attributes(getAttribute(RetrospectUpdateRequest.class, "isPublic")),
    };

    FieldDescriptor[] RETROSPECT_UPDATE_RESPONSE_FIELDS = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] RETROSPECT_READ_RESPONSE_FIELDS = {
        fieldWithPath("data").type(ARRAY).description("데이터"),
        fieldWithPath("data.[].id").type(NUMBER).description("회고 ID"),
        fieldWithPath("data.[].title").type(STRING).description("회고 제목"),
        fieldWithPath("data.[].content").type(STRING).description("회고 내용"),
        fieldWithPath("data.[].date").type(STRING).description("날짜"),
        fieldWithPath("data.[].isPublic").type(BOOLEAN).description("공개 여부"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestCookiesSnippet ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(TOKEN_COOKIE_DESCRIPTORS);
    PathParametersSnippet RETROSPECT_PATH_PARAMETER_SNIPPET = pathParameters(RETROSPECT_ID_PATH_PARAMETER_DESCRIPTORS);

    RequestFieldsSnippet RETROSPECT_CREATE_REQUEST_SNIPPET = requestFields(RETROSPECT_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet RETROSPECT_CREATE_RESPONSE_SNIPPET = responseFields(RETROSPECT_CREATE_RESPONSE_FIELDS);

    RequestFieldsSnippet RETROSPECT_UPDATE_REQUEST_SNIPPET = requestFields(RETROSPECT_UPDATE_REQUEST_FIELDS);
    ResponseFieldsSnippet RETROSPECT_UPDATE_RESPONSE_SNIPPET = responseFields(RETROSPECT_UPDATE_RESPONSE_FIELDS);

    PathParametersSnippet RETROSPECT_PAGING_PATH_PARAMETER_SNIPPET = pathParameters(RETROSPECT_PAGING_PATH_PARAMETER_DESCRIPTORS);
    ResponseFieldsSnippet RETROSPECT_READ_RESPONSE_SNIPPET = responseFields(RETROSPECT_READ_RESPONSE_FIELDS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
