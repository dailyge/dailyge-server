package project.dailyge.app.test.coupon.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static project.dailyge.app.common.SnippetUtils.*;

import java.util.List;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import project.dailyge.app.core.coupon.presentation.request.CouponCreateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;

public interface CouponSnippet {
	String TAG = "Coupon";
	String identifier = "{class_name}/{method_name}/";
	HeaderDescriptor HEADER_DESCRIPTOR = headerWithName("Authorization").description("인증 토큰");
	RequestHeadersSnippet COUPON_AUTHORIZATION_HEADER = requestHeaders(
		List.of(HEADER_DESCRIPTOR)
	);

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
