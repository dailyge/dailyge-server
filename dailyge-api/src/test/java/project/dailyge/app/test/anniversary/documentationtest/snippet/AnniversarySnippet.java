package project.dailyge.app.test.anniversary.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.cookies.CookieDescriptor;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import static project.dailyge.app.common.SnippetUtils.getAttribute;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryCreateRequest;

public interface AnniversarySnippet {

    String TAG = "Anniversary";
    String identifier = "{class_name}/{method_name}/";

    CookieDescriptor[] TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    FieldDescriptor[] ANNIVERSARY_CREATE_REQUEST_FIELDS = {
        fieldWithPath("name").description("기념일")
            .attributes(getAttribute(AnniversaryCreateRequest.class, "name")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(AnniversaryCreateRequest.class, "date")),
        fieldWithPath("remind").description("리마인드"),
        fieldWithPath("emojiId").description("리마인드")
            .attributes(getAttribute(AnniversaryCreateRequest.class, "emojiId")),
    };

    FieldDescriptor[] ANNIVERSARY_CREATE_RESPONSE = {
        fieldWithPath("data.anniversaryId").type(NUMBER).description("기념일 PK"),
        fieldWithPath("data").type(OBJECT).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestCookiesSnippet ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(TOKEN_COOKIE_DESCRIPTORS);
    RequestFieldsSnippet ANNIVERSARY_CREATE_REQUEST_SNIPPET = requestFields(ANNIVERSARY_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet ANNIVERSARY_CREATE_RESPONSE_SNIPPET = responseFields(ANNIVERSARY_CREATE_RESPONSE);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
