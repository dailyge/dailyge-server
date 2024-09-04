package project.dailyge.app.test.user.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import project.dailyge.app.user.presentation.request.UserBlacklistCreateRequest;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface UserSnippet {

    String TAG = "user";

    CookieDescriptor[] USER_BLACKLIST_ACCESS_TOKEN_COOKIE_DESCRIPTOR = {
        cookieWithName("Access-Token").description("사용자 토큰 쿠키")
    };

    RequestCookiesSnippet USER_BLACKLIST_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(USER_BLACKLIST_ACCESS_TOKEN_COOKIE_DESCRIPTOR);

    FieldDescriptor[] USER_BLACKLIST_CREATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("accessToken").description("인증 토큰").attributes(getAttribute(UserBlacklistCreateRequest.class, "accessToken")),
    };

    ParameterDescriptor[] USER_BLACKLIST_CREATE_PATH_DESCRIPTOR = {
        parameterWithName("userId").description("사용자 ID")
    };

    FieldDescriptor[] USER_BLACKLIST_CREATE_RESPONSE_FIELDS_DESCRIPTORS = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet USER_BLACKLIST_CREATE_RESPONSE_SNIPPET = requestFields(USER_BLACKLIST_CREATE_REQUEST_FIELDS_DESCRIPTORS);
    PathParametersSnippet USER_BLACKLIST_CREATE_PATH_PARAMETER_SNIPPET = pathParameters(USER_BLACKLIST_CREATE_PATH_DESCRIPTOR);
    ResponseFieldsSnippet USER_BLACKLIST_CREATE_RESPONSE_FIELDS_SNIPPET = responseFields(USER_BLACKLIST_CREATE_RESPONSE_FIELDS_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
