package project.dailyge.app.test.coupon.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import project.dailyge.app.core.coupon.presentation.request.CouponCreateRequest;

import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface CouponSnippet {
    String TAG = "Coupon";
    String identifier = "{class_name}/{method_name}/";
    CookieDescriptor[] COUPON_TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };
    RequestCookiesSnippet COUPON_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(COUPON_TOKEN_COOKIE_DESCRIPTORS);
    FieldDescriptor[] COUPON_CREATE_REQUEST_FIELDS = {
        fieldWithPath("dateTime").description("날짜 및 시간")
            .attributes(getAttribute(CouponCreateRequest.class, "dateTime")),
    };

    FieldDescriptor[] COUPON_CREATE_RESPONSE_FIELDS = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ERROR_RESPONSE = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    //coupon create participate
    RequestFieldsSnippet COUPON_CREATE_REQUEST_SNIPPET = requestFields(COUPON_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet COUPON_CREATE_RESPONSE_SNIPPET = responseFields(COUPON_CREATE_RESPONSE_FIELDS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
