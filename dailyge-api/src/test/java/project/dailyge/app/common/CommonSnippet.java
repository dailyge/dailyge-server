package project.dailyge.app.common;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.cookies.CookieDescriptor;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface CommonSnippet {

    CookieDescriptor[] TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    RequestCookiesSnippet ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(TOKEN_COOKIE_DESCRIPTORS);

    HeaderDescriptor[] COOKIE_HEADER_DESCRIPTORS = {
        headerWithName("Cookie").description("Access-Token=인증 토큰")
    };

    FieldDescriptor[] ERROR_RESPONSE = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };
}
