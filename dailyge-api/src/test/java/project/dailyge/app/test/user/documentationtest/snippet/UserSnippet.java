package project.dailyge.app.test.user.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.cookies.ResponseCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public interface UserSnippet {

    String TAG = "User";

    CookieDescriptor[] USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR = {
        cookieWithName("dg_sess").description("사용자 토큰 쿠키")
    };

    RequestCookiesSnippet USER_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR);

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

    ParameterDescriptor[] USER_UPDATE_PATH_DESCRIPTOR = {
        parameterWithName("userId").description("사용자 ID")
    };

    FieldDescriptor[] USER_UPDATE_REQUEST_FIELD_DESCRIPTOR = {
        fieldWithPath("nickname").type(STRING).description("닉네임")
    };

    FieldDescriptor[] USER_UPDATE_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] USER_DELETE_PATH_DESCRIPTOR = {
        parameterWithName("userId").description("사용자 ID")
    };

    CookieDescriptor[] USER_DELETE_RESPONSE_COOKIE_DESCRIPTOR = {
        cookieWithName("dg_sess").description("만료 된 인증 토큰"),
        cookieWithName("dg_res").description("만료 된 리프레시 토큰"),
        cookieWithName("logged_in").description("로그인 여부")
    };

    FieldDescriptor[] LOGIN_PAGE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.url").type(NUMBER).description("로그인 URL"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    CookieDescriptor[] LOGOUT_RESPONSE_COOKIE_DESCRIPTOR = {
        cookieWithName("dg_sess").description("만료 된 인증 토큰"),
        cookieWithName("dg_res").description("만료 된 리프레시 토큰"),
        cookieWithName("logged_in").description("로그인 여부")
    };

    FieldDescriptor[] RESPONSE_STATUS = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    // Search
    PathParametersSnippet USER_SEARCH_PATH_PARAMETER_SNIPPET = pathParameters(USER_SEARCH_PATH_DESCRIPTOR);
    ResponseFieldsSnippet USER_SEARCH_RESPONSE_SNIPPET = responseFields(USER_SEARCH_RESPONSE_FIELD_DESCRIPTOR);

    // Update
    RequestFieldsSnippet USER_UPDATE_REQUEST_FIELD_SNIPPET = requestFields(USER_UPDATE_REQUEST_FIELD_DESCRIPTOR);
    PathParametersSnippet USER_UPDATE_PATH_PARAMETER_SNIPPET = pathParameters(USER_UPDATE_PATH_DESCRIPTOR);
    ResponseFieldsSnippet USER_UPDATE_RESPONSE_FIELD_SNIPPET = responseFields(USER_UPDATE_RESPONSE_FIELD_DESCRIPTOR);

    // Delete
    PathParametersSnippet USER_DELETE_PATH_PARAMETER_SNIPPET = pathParameters(USER_DELETE_PATH_DESCRIPTOR);
    ResponseCookiesSnippet USER_DELETE_RESPONSE_COOKIE_SNIPPET = responseCookies(USER_DELETE_RESPONSE_COOKIE_DESCRIPTOR);

    // Login
    ResponseFieldsSnippet LOGIN_PAGE_RESPONSE_SNIPPET = responseFields(LOGIN_PAGE_FIELD_DESCRIPTOR);

    // Logout
    ResponseCookiesSnippet LOGOUT_RESPONSE_COOKIE_SNIPPET = responseCookies(LOGOUT_RESPONSE_COOKIE_DESCRIPTOR);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
