package project.dailyge.app.common;

import org.springframework.restdocs.cookies.CookieDescriptor;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import org.springframework.restdocs.headers.HeaderDescriptor;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

public interface CommonSnippet {
    CookieDescriptor[] TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    HeaderDescriptor[] COOKIE_HEADER_DESCRIPTORS = {
        headerWithName("Cookie").description("Access-Token=인증 토큰")
    };
}
