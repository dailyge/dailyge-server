package project.dailyge.app.test.user.documentationtest.snippet;

import org.springframework.restdocs.cookies.ResponseCookiesSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public interface UserSnippet {

    String TAG = "User";

    HeaderDescriptor HEADER_DESCRIPTOR = headerWithName("Authorization").description("인증 토큰").optional();

    RequestHeadersSnippet USER_AUTHORIZATION_HEADER = requestHeaders(
        headerWithName("Authorization").description("인증 토큰").optional()
    );

    ParameterDescriptor[] USER_SEARCH_PATH_DESCRIPTOR = {
        parameterWithName("userId").description("사용자 ID")
    };

    FieldDescriptor[] USER_SEARCH_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.userId").type(NUMBER).description("사용자 ID"),
        fieldWithPath("data.email").type(STRING).description("이메일"),
        fieldWithPath("data.nickname").type(STRING).description("닉네임"),
        fieldWithPath("data.profileImageUrl").type(STRING).description("사용자 프로필 URL"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] USER_DELETE_PATH_DESCRIPTOR = {
        parameterWithName("userId").description("사용자 ID")
    };

    FieldDescriptor[] LOGIN_PAGE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.url").type(NUMBER).description("로그인 URL"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ResponseCookiesSnippet LOGOUT_RESPONSE_COOKIE_SNIPPET = responseCookies(
        cookieWithName("Refresh-Token").description("리프레시 토큰")
    );

    FieldDescriptor[] RESPONSE_STATUS = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    // Search
    PathParametersSnippet USER_SEARCH_PATH_PARAMETER_SNIPPET = pathParameters(USER_SEARCH_PATH_DESCRIPTOR);
    ResponseFieldsSnippet USER_SEARCH_RESPONSE_SNIPPET = responseFields(USER_SEARCH_RESPONSE_FIELD_DESCRIPTOR);

    // Delete
    PathParametersSnippet USER_DELETE_PATH_PARAMETER_SNIPPET = pathParameters(USER_DELETE_PATH_DESCRIPTOR);

    // Login
    ResponseFieldsSnippet LOGIN_PAGE_RESPONSE_SNIPPET = responseFields(LOGIN_PAGE_FIELD_DESCRIPTOR);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
