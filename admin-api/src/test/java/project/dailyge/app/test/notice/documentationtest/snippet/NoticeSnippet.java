package project.dailyge.app.test.notice.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import project.dailyge.app.notice.presentation.request.NoticeCreateRequest;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface NoticeSnippet {

    String tag = "notice";

    CookieDescriptor[] NOTICE_ACCESS_TOKEN_COOKIE_DESCRIPTOR = {
        cookieWithName("Access-Token").description("사용자 토큰 쿠키")
    };

    RequestCookiesSnippet NOTICE_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(NOTICE_ACCESS_TOKEN_COOKIE_DESCRIPTOR);

    FieldDescriptor[] NOTICE_CREATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("title").description("공지사항 제목").attributes(getAttribute(NoticeCreateRequest.class, "title")),
        fieldWithPath("content").description("공지사항 내용").attributes(getAttribute(NoticeCreateRequest.class, "content")),
        fieldWithPath("noticeType").description("공지사항 유형").attributes(getAttribute(NoticeCreateRequest.class, "noticeType"))
    };

    FieldDescriptor[] NOTICE_CREATE_RESPONSE_FIELDS_DESCRIPTORS = {
        fieldWithPath("data.noticeId").type(NUMBER).description("공지사항 ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet NOTICE_CREATE_REQUEST_FIELDS_SNIPPET = requestFields(NOTICE_CREATE_REQUEST_FIELDS_DESCRIPTORS);
    ResponseFieldsSnippet NOTICE_CREATE_RESPONSE_FIELDS_SNIPPET = responseFields(NOTICE_CREATE_RESPONSE_FIELDS_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
